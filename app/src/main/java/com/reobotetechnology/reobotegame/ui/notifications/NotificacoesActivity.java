package com.reobotetechnology.reobotegame.ui.notifications;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.NotificationsAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.ui.match.LoadingMatchActivity;
import com.reobotetechnology.reobotegame.ui.messages.MessagesListActivity;
import com.reobotetechnology.reobotegame.ui.settings.ConfiguracoesActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificacoesActivity extends AppCompatActivity {

    //SwipeRefresh
    private SwipeRefreshLayout swipeRefresh;
    ConstraintLayout constraintMain;
    ConstraintLayout constraintNotifications;
    RecyclerView recyclerNotifications, recyclerNotificationsRecent, recyclerNotificationsYesterday, recyclerNotificationsDay;
    ConstraintLayout constraintNotificationsAll, constraintNotificationsRecent, constraintNotificationsDay, constraintNotificationsYesterday;
    NotificationsAdapters adapter;
    List<Notification> lista = new ArrayList<>();

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private DatabaseReference mDatabase;

    private FirebaseUser user = autenticacao.getCurrentUser();

    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back, btn_settings;


    //0 notifications
    private ImageView img_not;
    private TextView txt_not;
    private TextView txt_subtitle;

    private BottomSheetDialog bottomSheetDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);


        swipeRefresh = findViewById(R.id.swipe);
        //Refresh
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        onStart();
                        swipeRefresh.setRefreshing(false);
                    }
                }, 2000);

            }
        });
        constraintMain = findViewById(R.id.constraintPrincipal);
        constraintNotifications = findViewById(R.id.constraintNotifications);
        progressBar = findViewById(R.id.progressBar3);
        recyclerNotifications = findViewById(R.id.recyclerNotifications);
        recyclerNotificationsRecent = findViewById(R.id.recyclerNotificationsRecent);
        recyclerNotificationsDay = findViewById(R.id.recyclerNotificationsDay);
        recyclerNotificationsYesterday = findViewById(R.id.recyclerNotificationsYesterday);

        constraintNotificationsAll = findViewById(R.id.constraintNotificationsAll);
        constraintNotificationsRecent = findViewById(R.id.constraintNotificationsRecent);
        constraintNotificationsDay = findViewById(R.id.constraintNotificationsDay);
        constraintNotificationsYesterday = findViewById(R.id.constraintNotificationsYesterday);


        img_not = findViewById(R.id.img_not);
        txt_not = findViewById(R.id.txt_not);
        progressBar.setVisibility(View.VISIBLE);
        constraintMain.setVisibility(View.GONE);
        constraintNotifications.setVisibility(View.GONE);

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.notifica_es));


        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_settings = findViewById(R.id.btn_settings);
        btn_settings.setVisibility(View.VISIBLE);

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ConfiguracoesActivity.class));
            }
        });


        adapter = new NotificationsAdapters(lista, getApplicationContext());

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerNotifications.setLayoutManager(layoutManager);
        recyclerNotifications.setHasFixedSize(true);
        recyclerNotifications.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        //Configurar evento de clique no recyclerview
        recyclerNotifications.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerNotifications,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onItemClick(View view, int position) {

                                Notification notification = lista.get(position);
                                String idUsuario = Base64Custom.decodificarBase64(notification.getFromId());
                                String idUpdate = "" + notification.getTimestamp();
                                String type = notification.getTipo();

                                if (type.equals("partida")) {
                                    showInviteFriend(notification.getFromName(), notification.getId(), idUsuario, idUpdate, notification.getFromImage());
                                } else if (type.equals("chat")) {
                                    showMessageChat(idUpdate);

                                }
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getAllNotifications() {
        try {
            final String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
            firebaseRef.child("notifications").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    lista.clear();

                    try {

                        int countNoitificationsView = 0;

                        for (DataSnapshot dados : dataSnapshot.getChildren()) {

                            Notification notification = new Notification();

                            String fromName = dados.child("fromName").getValue().toString();
                            String fromImage = dados.child("fromImage").getValue().toString();
                            String tipo = dados.child("tipo").getValue().toString();
                            String id = dados.child("id").getValue().toString();
                            String fromId = dados.child("fromId").getValue().toString();
                            String toId = dados.child("toId").getValue().toString();
                            String timestamp = dados.child("timestamp").getValue().toString();
                            String text = dados.child("text").getValue().toString();
                            String view = dados.child("view").getValue().toString();
                            String date = dados.child("date").getValue().toString();
                            String time = dados.child("time").getValue().toString();

                            notification.setFromId(fromId);
                            notification.setToId(toId);
                            notification.setTimestamp(timestamp);
                            notification.setFromImage(fromImage);
                            notification.setText(text);

                            notification.setTipo(tipo);
                            notification.setFromName(fromName);
                            notification.setId(id);
                            notification.setView(Boolean.parseBoolean(view));
                            notification.setDate(date);
                            notification.setTime(time);

                            lista.add(notification);

                            if (!notification.isView()) {
                                countNoitificationsView = countNoitificationsView + 1;
                            }

                            txt_subtitle.setText(Html.fromHtml("Você possui <b>" + countNoitificationsView + "</b> nova(s) notificaçoes"));

                        }


                    } catch (Exception e) {
                        Log.i("ERRO NOTIFICATION", e.getMessage());
                    }


                    adapter.notifyDataSetChanged();

                    new Handler().postDelayed(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {

                            if (lista.size() == 0) {
                                constraintNotifications.setVisibility(View.VISIBLE);
                                constraintNotificationsAll.setVisibility(View.GONE);
                                constraintNotificationsRecent.setVisibility(View.GONE);
                                constraintNotificationsDay.setVisibility(View.GONE);
                                constraintNotificationsYesterday.setVisibility(View.GONE);

                                txt_subtitle.setText(getString(R.string.notifications_404));
                            } else {
                                constraintNotifications.setVisibility(View.GONE);
                                //PROVISÓRIO
                                constraintNotificationsAll.setVisibility(View.VISIBLE);
                                constraintNotificationsRecent.setVisibility(View.GONE);
                                constraintNotificationsDay.setVisibility(View.GONE);
                                constraintNotificationsYesterday.setVisibility(View.GONE);

                            }

                            progressBar.setVisibility(View.GONE);
                            constraintMain.setVisibility(View.VISIBLE);
                            btn_back.setAnimation(topAnim);

                        }
                    }, 2000);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {
            constraintNotifications.setVisibility(View.VISIBLE);
            recyclerNotifications.setVisibility(View.GONE);
            txt_subtitle.setText(getString(R.string.notifications_404));

            constraintMain.setVisibility(View.VISIBLE);
            btn_back.setAnimation(topAnim);
        }
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showInviteFriend(final String nome, final String idPartida, final String email, final String idUpdate, final String imagem) {
        @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.include_bottom_sheet_invite_open_friend, null);

        String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
        DatabaseReference usuarioRef2 = firebaseRef.child("notifications").child(idUsuario).child(idUpdate);
        usuarioRef2.child("view").setValue(true);

        TextView title = view.findViewById(R.id.textView20);
        title.setText("Vamos Jogar?");

        TextView descriptionInvite = view.findViewById(R.id.textView26);
        String[] nomeModificed = Objects.requireNonNull(nome).split(" ");

        descriptionInvite.setText(nomeModificed[0] + " está lhe desafiando");

        CircleImageView profile = view.findViewById(R.id.profile);

        Glide
                .with(view)
                .load(imagem)
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(profile);


        view.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        view.findViewById(R.id.button8).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

                    DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);


                    usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            try {

                                String desconectado = Objects.requireNonNull(dataSnapshot.child("desconectado").getValue()).toString();

                                String recusado = Objects.requireNonNull(dataSnapshot.child("recusado").getValue()).toString();

                                String resultado = Objects.requireNonNull(dataSnapshot.child("resultado").getValue()).toString();


                                if (!resultado.equals("")) {
                                    Toast.makeText(getApplicationContext(), "Essa partida já foi finalizada!", Toast.LENGTH_LONG).show();

                                } else if (recusado.equals("true")) {
                                    Toast.makeText(getApplicationContext(), "OOH NÃO!\nO jogador(a) " + nome + " recusou o seu convite", Toast.LENGTH_LONG).show();

                                } else if (desconectado.equals("true")) {
                                    Toast.makeText(getApplicationContext(), "OOH NÃO!\nO jogador(a) " + nome + " desistiu da partida", Toast.LENGTH_LONG).show();

                                } else {


                                    Intent intent = new Intent(getApplicationContext(), LoadingMatchActivity.class);
                                    intent.putExtra("token", "");
                                    intent.putExtra("email", email);
                                    intent.putExtra("convidado", "sim");
                                    intent.putExtra("idPartida", idPartida);

                                    DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
                                    usuarioRef.child("aceito").setValue(true);


                                    startActivity(intent);

                                    finish();

                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "OOH NÃO!\nO jogador(a) " + nome + " desistiu da partida", Toast.LENGTH_LONG).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } catch (Exception ignored) {


                }


                bottomSheetDialog.dismiss();
            }
        });

        view.findViewById(R.id.button9).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
                usuarioRef.child("recusado").setValue(true);

                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
            }
        });

        bottomSheetDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showMessageChat(String idUpdate) {
        try {
            String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
            DatabaseReference usuarioRef2 = firebaseRef.child("notifications").child(idUsuario).child(idUpdate);
            usuarioRef2.child("view").setValue(true);

            Intent i = new Intent(getApplicationContext(), MessagesListActivity.class);
            i.putExtra("idUpdate", idUpdate);
            startActivity(i);
        } catch (Exception ignored) {

        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();
        getAllNotifications();

    }


}
