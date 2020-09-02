package com.reobotetechnology.reobotegame.ui.notificações;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Constraints;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.NotificationsAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.ui.partida.CarregarPartidaActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NotificacoesActivity extends AppCompatActivity {

    RecyclerView recyclerNotifications;
    NotificationsAdapters adapter;
    List<Notification> lista = new ArrayList<>();

    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    private ProgressBar progressBar;
    private ImageView img_not;
    private TextView txt_not;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacoes);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle("Notificações");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progressBar);
        recyclerNotifications = findViewById(R.id.recyclerNotifications);
        img_not = findViewById(R.id.img_not);
        txt_not = findViewById(R.id.txt_not);
        progressBar.setVisibility(View.VISIBLE);
        img_not.setVisibility(View.GONE);
        txt_not.setVisibility(View.GONE);
        recyclerNotifications.setVisibility(View.GONE);



        adapter = new NotificationsAdapters(lista, getApplicationContext());

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerNotifications.setLayoutManager(layoutManager);
        recyclerNotifications.setHasFixedSize(true);
        recyclerNotifications.setAdapter(adapter);

        //Configurar evento de clique no recyclerview
        recyclerNotifications.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerNotifications,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onItemClick(View view, int position) {

                                Notification notification = lista.get( position );
                                String idUsuario = Base64Custom.decodificarBase64(notification.getFromId());
                                String idExcluir = notification.getToId();
                                modalConvite(notification.getFromName(), notification.getIdPartida(), idUsuario, idExcluir, notification.getFromImage());
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

    private void getAllNotifications() {
        try {
            String idUsuario = Base64Custom.codificarBase64(user.getEmail());
            firebaseRef.child("notifications").orderByChild(idUsuario).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    lista.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Notification notification = dados.getValue(Notification.class);

                        assert notification != null;

                        lista.add(notification);

                    }

                    adapter.notifyDataSetChanged();

                    if (lista.size() == 0) {
                        img_not.setVisibility(View.VISIBLE);
                        txt_not.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                        recyclerNotifications.setVisibility(View.VISIBLE);
                    } else {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                adapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerNotifications.setVisibility(View.VISIBLE);

                            }
                        }, 2000);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e ){
            progressBar.setVisibility(View.GONE);
            img_not.setVisibility(View.VISIBLE);
            txt_not.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
            recyclerNotifications.setVisibility(View.GONE);
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void modalConvite(final String nome, final String idPartida, final String email, final String idExcluir, final String imagem) {


        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        View mView = getLayoutInflater().inflate(R.layout.modal_convidar, null);
        alertDialog.setCancelable(false);

        final CircleImageView img_jogador_invite = mView.findViewById(R.id.img_jogador_invite);
        final Button btnAceitar = mView.findViewById(R.id.btnAceitar);
        final Button btnRecusar = mView.findViewById(R.id.btnRecusar);
        final TextView txtConvite = mView.findViewById(R.id.txtDescJogador);
        txtConvite.setText("O jogador(a): " + nome + " está te convidando para jogar uma partida online.\n\nDeseja aceitar o desafio?");

        try {
            if (imagem.isEmpty()) {
                //Picasso.get().load(R.drawable.user).into(holder.img);
                img_jogador_invite.setImageResource(R.drawable.user);
            } else {

                Glide
                        .with(getApplicationContext())
                        .load(imagem)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(img_jogador_invite);
            }
        } catch (Exception e) {
            img_jogador_invite.setImageResource(R.drawable.user);
        }

        alertDialog.setView(mView);
        final AlertDialog alert = alertDialog.create();
        ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
        Objects.requireNonNull(alert.getWindow()).setLayout(Constraints.LayoutParams.WRAP_CONTENT, Constraints.LayoutParams.WRAP_CONTENT);
        InsetDrawable inset = new InsetDrawable(back, 130);
        Objects.requireNonNull(alert.getWindow()).setBackgroundDrawable(inset);
        alert.show();

        btnAceitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);

                    usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            try {

                                String desconectado = Objects.requireNonNull(dataSnapshot.child("desconectado").getValue()).toString();

                                if (desconectado.equals("true")) {
                                    Toast.makeText(getApplicationContext(), "OOH NÃO!\nO jogador(a) " + nome + " desistiu da partida", Toast.LENGTH_LONG).show();
                                    alert.dismiss();
                                } else {

                                    alert.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), CarregarPartidaActivity.class);
                                    intent.putExtra("token", "");
                                    intent.putExtra("email", email);
                                    intent.putExtra("convidado", "sim");
                                    intent.putExtra("idPartida", idPartida);

                                    DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
                                    usuarioRef.child("aceito").setValue(true);
                                    //*NÃO ESQUECER DE MUDAR PARA INVES DE EXCLUIR A NOTIFICAÇÃO APENAS COLOCAR VISUALIZADO == TRUE*//
                                    DatabaseReference usuarioRef2 = firebaseRef.child("notifications");
                                    //usuarioRef2.child("view").setValue(true);
                                    usuarioRef2.child(idExcluir).removeValue();
                                    startActivity(intent);

                                    finish();

                                }

                            } catch (Exception e) {


                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } catch (Exception e) {

                }

            }
        });

        btnRecusar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alert.dismiss();
                //*NÃO ESQUECER DE MUDAR PARA INVES DE EXCLUIR A NOTIFICAÇÃO APENAS COLOCAR VISUALIZADO == TRUE*//
                DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
                usuarioRef.child("recusado").setValue(true);
                DatabaseReference usuarioRef2 = firebaseRef.child("notifications");
                //usuarioRef2.child("view").setValue(true);
                usuarioRef2.child(idExcluir).removeValue();

            }
        });


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Finaliza a Activity atual e assim volta para a tela anterior
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        getAllNotifications();
    }


}
