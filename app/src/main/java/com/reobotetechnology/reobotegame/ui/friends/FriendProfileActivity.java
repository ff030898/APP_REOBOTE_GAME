package com.reobotetechnology.reobotegame.ui.friends;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.BooksOfBibleAdapters;
import com.reobotetechnology.reobotegame.adapter.ProfileConquistesAdapters;
import com.reobotetechnology.reobotegame.adapter.ProfileMatchesAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.ConquistesModel;
import com.reobotetechnology.reobotegame.model.BooksOfBibleModel;
import com.reobotetechnology.reobotegame.model.MatchModel;
import com.reobotetechnology.reobotegame.model.Message;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.bible.ChaptersActivity;
import com.reobotetechnology.reobotegame.ui.bible.ListBiblieScreen;
import com.reobotetechnology.reobotegame.ui.match.MatchLoadingActivity;
import com.reobotetechnology.reobotegame.utils.VerificyFollowUser;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendProfileActivity extends AppCompatActivity {


    private ProgressBar progressBar;
    private CoordinatorLayout constraintPrincipal;
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();


    //Animation
    private Animation topAnim;
    private ImageButton btn_back;

    //Toolbar
    private TextView txt_title, txt_subtitle;

    //Status
    private CircleImageView profile_main;
    private TextView txt_username;
    private TextView txt_email;
    private TextView txtRankingUsuarioPerfil;
    private TextView txtSeguindoUsuarioPerfil;
    private TextView txtSeguidoresUsuarioPerfil;
    private TextView bioDescription;
    private String id, nome, imagem;
    private FloatingActionButton btn_edit;

    //Status Info Completed
    private TextView txtNivelStatus;
    private TextView txtRankingStatus;
    private TextView txtSeguindoStatus;
    private TextView txtSeguidoresStatus;
    private TextView txtVictoryStatus;
    private TextView txtDerrotedStatus;
    private TextView txtEmpatedStatus;
    private TextView txtBioUserStatus;

    //List Conquist
    private ProfileConquistesAdapters adapterConquist;
    private ArrayList<ConquistesModel> listConquist = new ArrayList<>();

    // List book favorite
    private BooksOfBibleAdapters adapterFavorites;
    private List<BooksOfBibleModel> listFavorites = new ArrayList<>();
    private int tamanho = 0;

    // list Matches
    private ProfileMatchesAdapters adapterMatches;
    private List<MatchModel> listMatches = new ArrayList<>();

    //InviteFriend
    boolean match = true;
    String email, token;

    private Animation modal_anima;
    private int score;
    private int nivelUser;

    private boolean follow;
    private String child = "seguidores";
    private String child2 = "seguindo";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        progressBar = findViewById(R.id.progressBar3);
        constraintPrincipal = findViewById(R.id.constraintPrincipal);

        //TOOLBAR
        txt_title = findViewById(R.id.txt_title);
        txt_subtitle = findViewById(R.id.txt_subtitle);

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        constraintPrincipal.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Animação da Modal de Tempo Esgotado
        modal_anima = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.modal_animation);

        //status

        profile_main = findViewById(R.id.profile_main);

        profile_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), ViewImageScreenActivity.class);
                i.putExtra("nome", nome);
                i.putExtra("imagem", imagem);
                startActivity(i);
            }
        });
        txt_username = findViewById(R.id.txt_username);
        txt_email = findViewById(R.id.txt_email);
        txtRankingUsuarioPerfil = findViewById(R.id.txtRankingUsuarioPerfil);
        txtSeguindoUsuarioPerfil = findViewById(R.id.txtSeguindoUsuarioPerfil);
        txtSeguidoresUsuarioPerfil = findViewById(R.id.txtSeguidoresUsuarioPerfil);
        bioDescription = findViewById(R.id.textView13);
        TextView editBio = findViewById(R.id.editar);
        editBio.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        id = extras.getString("id");


        btn_edit = findViewById(R.id.btn_edit);

        btn_edit.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                followUser();
            }
        });

        //StatusCompleted
        txtNivelStatus = findViewById(R.id.textViewN2);
        txtRankingStatus = findViewById(R.id.textView29);
        txtSeguindoStatus = findViewById(R.id.textView33);
        txtSeguidoresStatus = findViewById(R.id.textView35);
        txtVictoryStatus = findViewById(R.id.textView37);
        txtEmpatedStatus = findViewById(R.id.textView39);
        txtDerrotedStatus = findViewById(R.id.textView41);
        txtBioUserStatus = findViewById(R.id.textView17);

        //RecyclerConquist
        RecyclerView recyclerRanking = findViewById(R.id.recyclerConquistes);
        adapterConquist = new ProfileConquistesAdapters(listConquist, getApplicationContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerRanking.setLayoutManager(layoutManager);
        recyclerRanking.setAdapter(adapterConquist);

        ImageView ic_new = findViewById(R.id.ic_novo);

        RecyclerView recyclerNovoTestamento = findViewById(R.id.recyclerLivrosNovo);

        adapterFavorites = new BooksOfBibleAdapters(listFavorites, getApplicationContext());

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerNovoTestamento.setLayoutManager(layoutManager2);
        recyclerNovoTestamento.setAdapter(adapterFavorites);

        ic_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ListBiblieScreen.class);
                i.putExtra("cd_testamento", 1);
                startActivity(i);
            }
        });

        //Recycler Novo
        recyclerNovoTestamento.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerNovoTestamento,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                tamanho = listFavorites.size();

                                if (tamanho > 2) {

                                    BooksOfBibleModel livroSelecionado = listFavorites.get(position);
                                    Intent i = new Intent(getApplicationContext(), ChaptersActivity.class);
                                    i.putExtra("nm_livro", livroSelecionado.getNome());
                                    i.putExtra("livroSelecionado", livroSelecionado.getId());
                                    startActivity(i);

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

        //Recycler Conquist
        recyclerRanking.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerRanking,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onItemClick(View view, int position) {
                                ConquistesModel nivel = listConquist.get(position);
                                //Se nível está ativo e não for o primeiro nível. OpenModal
                                if (nivel.isEnabled() && position > 0) {
                                    openModalConquist(nivel.getDescription(), nivel.getCount());
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

        //List Matches

        RecyclerView recyclerMatch = findViewById(R.id.recyclerMatches);
        adapterMatches = new ProfileMatchesAdapters(listMatches, getApplicationContext());

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(getApplicationContext());
        recyclerMatch.setLayoutManager(layoutManager5);
        recyclerMatch.setAdapter(adapterMatches);

        Button buttonInviteFriend = findViewById(R.id.buttonInviteFriend);
        buttonInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inviteFriend();
            }
        });

        //OpenFriendsList
        txtSeguindoUsuarioPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FriendsListActivity.class);
                i.putExtra("eventList", getString(R.string.seguindoMin));
                startActivity(i);

            }
        });

        txtSeguidoresUsuarioPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), FriendsListActivity.class);
                i.putExtra("eventList", getString(R.string.seguidoresMin));
                startActivity(i);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getUser() {

        if (autenticacao.getCurrentUser() != null) {

            String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(id));

            firebaseRef.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserModel user = dataSnapshot.getValue(UserModel.class);

                    if (user != null) {

                        match = user.isJogando();
                        email = user.getEmail();
                        token = user.getToken();
                        nome = user.getNome();
                        imagem = user.getImagem();
                        score = user.getPontosG();
                        listConquist(score);
                        nivelUser = user.getNivel();

                        //Toolbar
                        txt_title.setText(nome);
                        txt_subtitle.setText("Informações do jogador");

                        //STATUS
                        txt_username.setText(nome);
                        txt_email.setText("Nível: " + user.getNivel());
                        txtRankingUsuarioPerfil.setText(user.getRanking() + "º");
                        txtSeguindoUsuarioPerfil.setText("" + user.getSeguindo());
                        txtSeguidoresUsuarioPerfil.setText("" + user.getSeguidores());
                        bioDescription.setText("SOBRE MIM");

                        //INFO
                        txtNivelStatus.setText(user.getNivel() + "");
                        txtRankingStatus.setText(user.getRanking() + "");
                        txtSeguindoStatus.setText("" + user.getSeguindo());
                        txtSeguidoresStatus.setText("" + user.getSeguidores());
                        txtVictoryStatus.setText("" + user.getVitorias());
                        txtEmpatedStatus.setText("" + user.getEmpates());
                        txtDerrotedStatus.setText("" + user.getDerrotas());
                        String description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. is. Sed tempus laoreea. " +
                                "Ut vitae neque venenatis neque facilisis pellentesque. Sed tincidunt laoreet mauris sed molestie. " +
                                "Duis sodales diam eu placerat dapibus.</string>\n";
                        if(user.getDescription().isEmpty() || user.getDescription() == null){
                            txtBioUserStatus.setText(description);
                        }else {
                            txtBioUserStatus.setText("" + user.getDescription());
                        }


                        try {

                            if (imagem.isEmpty()) {

                                Glide
                                        .with(getApplicationContext())
                                        .load(R.drawable.profile)
                                        .centerCrop()
                                        .placeholder(R.drawable.profile)
                                        .into(profile_main);
                            } else {
                                Glide
                                        .with(getApplicationContext())
                                        .load(imagem)
                                        .centerCrop()
                                        .placeholder(R.drawable.profile)
                                        .into(profile_main);
                            }
                        } catch (Exception e) {
                            Glide
                                    .with(getApplicationContext())
                                    .load(R.drawable.profile)
                                    .centerCrop()
                                    .placeholder(R.drawable.profile)
                                    .into(profile_main);
                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    adapterMatches.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    constraintPrincipal.setVisibility(View.VISIBLE);
                    btn_back.setAnimation(topAnim);

                }
            }, 2000);


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getFollow() {

        String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(id));
        final String idUserAuthenticate = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));

        firebaseRef.child(child).child(idUsuario).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try {
                    Objects.requireNonNull(dataSnapshot.child(idUserAuthenticate).getValue());
                    follow = true;
                    btn_edit.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.badge_background));
                    btn_edit.setImageResource(R.drawable.ic_done);


                } catch (Exception e) {
                    follow = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        btn_edit.setImageResource(R.drawable.ic_add);
                        btn_edit.setImageTintList(ColorStateList.valueOf(0xffffffff));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void followUser(){
        getFollow();

        final String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(id));
        final String idUserAuthenticate = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));

        if (!follow) {
            btn_edit.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.badge_background));
            btn_edit.setImageResource(R.drawable.ic_done);

            sendNotification(idUserAuthenticate, idUsuario);

            //Update Follow
            DatabaseReference follow = firebaseRef.child(child);
            String idUserAutenticate = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
            follow.child(idUsuario).child(idUserAutenticate).setValue(1);

            DatabaseReference follow2 = firebaseRef.child(child2);
            follow2.child(idUserAutenticate).child(idUsuario).setValue(1);

            firebaseRef.child("usuarios").child(idUserAuthenticate).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserModel user = dataSnapshot.getValue(UserModel.class);

                    if (user != null) {
                        int seguindoFollow = user.getSeguindo();
                        int newUpdateFollow = seguindoFollow + 1;
                        //Update Follow
                        DatabaseReference follow = firebaseRef.child("usuarios");
                        follow.child(idUserAuthenticate).child("seguindo").setValue(newUpdateFollow);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            firebaseRef.child("usuarios").child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UserModel user = dataSnapshot.getValue(UserModel.class);

                    if (user != null) {
                        int seguidores = user.getSeguidores();
                        int newUpdateFollow = seguidores + 1;
                        //Update Follow
                        DatabaseReference follow = firebaseRef.child("usuarios");
                        follow.child(idUsuario).child("seguidores").setValue(newUpdateFollow);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Toast.makeText(getApplicationContext(), "Você começou a seguir " + nome, Toast.LENGTH_LONG).show();

        } else {
            modalNowFollow(idUsuario);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void modalNowFollow(final String idUsuario) {
        try {

            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Deixa de Seguir")
                    .setContentText("Tem certeza que deseja deixar de seguir " + nome)
                    .setConfirmText("Sim")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            nowFollow(idUsuario);
                            sDialog.hide();
                            getFollow();
                        }
                    }).setCancelText("Não")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.hide();
                            getFollow();
                        }
                    })
                    .show();


        } catch (Exception ignored) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void nowFollow(final String idUsuario){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            btn_edit.setBackground(getApplicationContext().getResources().getDrawable(R.drawable.bg_ranking_position));
            btn_edit.setImageResource(R.drawable.ic_add);
            btn_edit.setImageTintList(ColorStateList.valueOf(0xffffffff));
        }

        final String idUserAuthenticate = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
        DatabaseReference usuarioRef = firebaseRef.child(child).child(idUsuario);
        usuarioRef.child(idUserAuthenticate).removeValue();

        DatabaseReference follow2 = firebaseRef.child(child2).child(idUserAuthenticate);
        follow2.child(idUsuario).removeValue();
        follow2.child(idUsuario).removeValue();

        firebaseRef.child("usuarios").child(idUsuario).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);

                if (user != null) {
                    int seguidores = user.getSeguidores();
                    int newUpdateFollow = seguidores - 1;
                    //Update Follow
                    DatabaseReference follow = firebaseRef.child("usuarios");
                    follow.child(idUsuario).child("seguidores").setValue(newUpdateFollow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        firebaseRef.child("usuarios").child(idUserAuthenticate).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserModel user = dataSnapshot.getValue(UserModel.class);

                if (user != null) {
                    int seguindo = user.getSeguindo();
                    int newUpdateFollow = seguindo - 1;
                    //Update Follow
                    DatabaseReference follow = firebaseRef.child("usuarios");
                    follow.child(idUserAuthenticate).child("seguindo").setValue(newUpdateFollow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Toast.makeText(getApplicationContext(), "Você deixou de seguir " + nome, Toast.LENGTH_LONG).show();
    }


    private void sendNotification(String idUsuario, String idUsuario2) {
        long timestamp = System.currentTimeMillis();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatNotification = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Calendar cal = Calendar.getInstance();
        Date data = new Date();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String dateNotification = dateFormatNotification.format(data);
        String time = timeFormat.format(data_atual);

        //Cria uma partida
        String idMessage = dateFormat.format(data_atual);

        final Message message = new Message();
        message.setFromId(idUsuario);
        message.setToId(idUsuario2);
        message.setTimestamp("" + timestamp);
        message.setText("Começou a seguir você");


        Notification notification = new Notification();
        notification.setFromId(message.getFromId());
        notification.setToId(message.getToId());
        notification.setTimestamp(message.getTimestamp());
        notification.setText(message.getText());
        notification.setTipo("follow");
        notification.setFromName(user.getDisplayName());
        notification.setId(idMessage);
        if (user.getPhotoUrl() != null) {
            notification.setFromImage(user.getPhotoUrl().toString());
        } else {
            notification.setFromImage("");
        }

        notification.setDate(dateNotification);
        notification.setTime(time);

        notification.setView(false);
        DatabaseReference usuarioRef = firebaseRef.child("notifications");
        usuarioRef.child(idUsuario2).child("" + timestamp).setValue(notification);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void listConquist(int score) {

        listConquist.clear();

        listConquist.add(new ConquistesModel("Cadastro", 20, true));

        int count = 1;
        int countNivel = 200;
        int nivelUser = 0;

        for (int i = countNivel; i <= 10000; i += countNivel) {

            if (score > i) {
                listConquist.add(new ConquistesModel("Nível " + count, i, true));
                nivelUser++;
            } else if ((i - score) <= countNivel) {
                listConquist.add(new ConquistesModel("Nível " + count, i, true));
                nivelUser++;
            } else {
                listConquist.add(new ConquistesModel("Nível " + count, i, false));
            }

            count++;
        }

        adapterConquist.notifyDataSetChanged();
        updateNivelUser(nivelUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateNivelUser(int nivel) {

        try {

            String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(id));
            DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
            usuarioRef.child("nivel").setValue(nivel);

        } catch (Exception ignored) {

        }

    }

    @SuppressLint("SetTextI18n")
    private void openModalConquist(String description, int count) {

        final Dialog nivelDIalog = new Dialog(this);
        nivelDIalog.setContentView(R.layout.include_modal_nivel);

        CardView cardModal = nivelDIalog.findViewById(R.id.cardModal);
        cardModal.startAnimation(modal_anima);

        ImageButton btn_close = nivelDIalog.findViewById(R.id.btn_close);
        ImageView image = nivelDIalog.findViewById(R.id.nivel);
        TextView txt_title = nivelDIalog.findViewById(R.id.txt_title);
        TextView txtDescription = nivelDIalog.findViewById(R.id.txtDescription);
        TextView txtProgresso = nivelDIalog.findViewById(R.id.txtProgresso);
        ProgressBar progressBar = nivelDIalog.findViewById(R.id.progressBar);
        progressBar.setMax(count);
        progressBar.setProgress(Math.min(score, count));

        txt_title.setText(description);
        if (score <= count) {
            txtProgresso.setText(score + "/" + count + " xp");
            String desc = "O Jogador(a) \n" + nome + " \nestá no " + description.toLowerCase();
            txtDescription.setText(desc);
        } else {
            image.setImageResource(R.drawable.ic_emogi_happy);
            txtProgresso.setText(count + "/" + count + " xp");
            String desc = "O Jogador(a) \n" + nome + " \nconquistou o " + description.toLowerCase();
            txtDescription.setText(desc);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(nivelDIalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nivelDIalog.dismiss();
            }
        });
        nivelDIalog.show();

    }

    private void listBookFavorites() {

        listFavorites.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

        List<BooksOfBibleModel> lista2;
        lista2 = dataBaseAcess.listarNovoTestamento();

        if (lista2.size() != 0) {
            for (int i = 0; i < lista2.size(); i++) {

                int learningBook = dataBaseAcess.learningBook(lista2.get(i).getId());
                boolean favorited = dataBaseAcess.favorited(lista2.get(i).getId());
                lista2.get(i).setLearning(learningBook);
                lista2.get(i).setFavorited(favorited);
                listFavorites.add(lista2.get(i));
            }
        }

        adapterFavorites.notifyDataSetChanged();


    }

    private void listMatches() {

        listMatches.clear();

        /*try {

            //firebaseRef.child("usuarios").orderByChild("pontosD").limitToLast(7)

            firebaseRef.child("partidas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listMatches.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        //PartidaModel p = dados.getValue(PartidaModel.class);


                        //String t = dados.getValue().toString();

                        //listMatches.add(p);

                        //Log.d("partida", "id: "+t);

                    }


                    adapterMatches.notifyDataSetChanged();
                    //progressBar.setVisibility(View.GONE);
                    //constraintPrincipal.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception ignored) {

        }*/

        listMatches.add(new MatchModel("27/02/2021 - 02:12", false, true, false, true, "d", "27/02/2021 - 02:12"));
        listMatches.add(new MatchModel("25/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));
        listMatches.add(new MatchModel("22/02/2021 - 02:12", false, true, false, true, "d", "27/02/2021 - 02:12"));
        listMatches.add(new MatchModel("23/02/2021 - 02:12", false, true, false, true, "v", "27/02/2021 - 02:12"));
        listMatches.add(new MatchModel("27/02/2021 - 02:12", false, true, false, true, "e", "27/02/2021 - 02:12"));


        adapterMatches.notifyDataSetChanged();


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void inviteFriend() {
        try {
            getUser();

            new SweetAlertDialog(FriendProfileActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Jogar Agora")
                    .setContentText("Tem certeza que deseja desafiar: " + nome + " para jogar agora?")
                    .setConfirmText("Sim")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            if (match) {
                                //Se usuario estiver jogando
                                String description = "Jogador(a): " + nome + " está em uma partida no momento. Aguarde 5 minutos ou convide outro amigo";
                                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_LONG).show();
                                sDialog.hide();
                            } else {

                                Intent i = new Intent(getApplicationContext(), MatchLoadingActivity.class);
                                i.putExtra("token", token);
                                i.putExtra("email", email);
                                i.putExtra("convidado", "nao");
                                i.putExtra("idPartida", "");
                                startActivity(i);
                                sDialog.hide();
                                finish();
                            }

                        }
                    }).setCancelText("Não")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.hide();
                        }
                    })
                    .show();

        } catch (Exception ignored) {

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onStart() {
        super.onStart();
        getUser();
        getFollow();
        listBookFavorites();
        listMatches();
    }
}
