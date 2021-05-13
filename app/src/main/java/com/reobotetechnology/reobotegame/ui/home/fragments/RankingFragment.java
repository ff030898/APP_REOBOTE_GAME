package com.reobotetechnology.reobotegame.ui.home.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.reobotetechnology.reobotegame.adapter.RankingAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.ConfigurationFirebase;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.Notification;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.reobotetechnology.reobotegame.ui.friends.FriendProfileActivity;
import com.reobotetechnology.reobotegame.ui.notifications.NotificationsActivity;
import com.reobotetechnology.reobotegame.utils.LinearLayoutManagerWithSmoothScroller;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class RankingFragment extends Fragment {

    //Toolbar
    private CircleImageView profileImage;
    private TextView textWelcome, textDescriptionNotifications;
    private Button btn_notifications;
    private ImageView top_ranking;

    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    //Top 5
    private RankingAdapters adapterRanking;
    private ArrayList<UserModel> listaRanking = new ArrayList<>();

    private ProgressBar progressBar;
    private CoordinatorLayout constraintPrincipal;


    private CircleImageView img_user, img_user2, img_user3;
    private TextView Jogador, Jogador2, Jogador3, pJogador, pJogador2, pJogador3;

    private int postitionRanking;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ranking, container, false);


        progressBar = root.findViewById(R.id.progressBar3);
        constraintPrincipal = root.findViewById(R.id.constraintPrincipal);


        //configuracoes de objetos
        autenticacao = ConfigurationFirebase.getFirebaseAutenticacao();
        user = autenticacao.getCurrentUser();


        progressBar.setVisibility(View.VISIBLE);
        constraintPrincipal.setVisibility(View.GONE);

        //Toolbar

        textWelcome = root.findViewById(R.id.textWelcome);
        textDescriptionNotifications = root.findViewById(R.id.textDescriptionNotifications);
        btn_notifications = root.findViewById(R.id.btn_notifications);
        top_ranking = root.findViewById(R.id.top_ranking);

        profileImage = root.findViewById(R.id.profile);

        btn_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), NotificationsActivity.class));
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), NotificationsActivity.class));
            }
        });

        img_user = root.findViewById(R.id.img_user);
        img_user2 = root.findViewById(R.id.img_user2);
        img_user3 = root.findViewById(R.id.img_user3);
        Jogador = root.findViewById(R.id.Jogador);
        Jogador2 = root.findViewById(R.id.Jogador2);
        Jogador3 = root.findViewById(R.id.Jogador3);
        pJogador = root.findViewById(R.id.pJogador);
        pJogador2 = root.findViewById(R.id.pJogador2);
        pJogador3 = root.findViewById(R.id.pJogador3);

        final RecyclerView recyclerRanking = root.findViewById(R.id.recyclerJogadores);
        adapterRanking = new RankingAdapters(listaRanking, getActivity());

        //RecyclerRanking
        recyclerRanking.setLayoutManager(new LinearLayoutManagerWithSmoothScroller(getActivity()));
        recyclerRanking.setHasFixedSize(true);
        recyclerRanking.setAdapter(adapterRanking);

        //Recycler Ranking
        recyclerRanking.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerRanking,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                int tamanho = listaRanking.size();
                                UserModel usuarioSelecionado = listaRanking.get((tamanho - position - 1));
                                if (!usuarioSelecionado.getEmail().equals(user.getEmail())) {
                                    Intent i = new Intent(getActivity(), FriendProfileActivity.class);
                                    i.putExtra("id", usuarioSelecionado.getEmail());
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

        top_ranking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerRanking.smoothScrollToPosition(postitionRanking - 1);
            }
        });

        return root;
    }

    //Profile

    //Usuario Logado
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void viewProfile() {

        if (autenticacao.getCurrentUser() != null) {


            try {
                if (user.getPhotoUrl() == null) {
                    Glide
                            .with(RankingFragment.this)
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(profileImage);
                } else {
                    Glide
                            .with(RankingFragment.this)
                            .load(user.getPhotoUrl())
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(profileImage);
                }

                top_ranking.setVisibility(View.VISIBLE);

                textWelcome.setText(getString(R.string.ranking));

                String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));

                firebaseRef.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        UserModel user = dataSnapshot.getValue(UserModel.class);
                        assert user != null;
                        postitionRanking = user.getRanking();
                        textDescriptionNotifications.setText("Você está em " + user.getRanking() + "º no ranking");
                        int backPosition = user.getBackPosition();

                        int sumPosition;


                        if (backPosition > postitionRanking) {
                            sumPosition = (backPosition - postitionRanking);
                            top_ranking.setImageResource(R.drawable.ic_position_down);

                            if (sumPosition >= 2) {
                                Toast.makeText(getActivity(), "Você caiu: " + sumPosition + " posições no ranking", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "Você caiu: " + sumPosition + " posição no ranking", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            top_ranking.setImageResource(R.drawable.ic_position_up);
                            sumPosition = (postitionRanking - backPosition);
                            if (sumPosition > 0) {
                                if (sumPosition >= 2) {
                                    Toast.makeText(getActivity(), "Você subiu: " + sumPosition + " posições no ranking", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getActivity(), "Você subiu: " + sumPosition + " posição no ranking", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            } catch (Exception e) {
                Glide
                        .with(RankingFragment.this)
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profileImage);
            }
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void getAllNotifications() {

        final String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
        firebaseRef.child("notifications").child(idUsuario).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                try {
                    int countNoitificationsView = 0;
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        Notification notification = new Notification();
                        String view = Objects.requireNonNull(dados.child("view").getValue()).toString();
                        notification.setView(Boolean.parseBoolean(view));
                        if (!notification.isView()) {
                            countNoitificationsView = countNoitificationsView + 1;
                        }
                    }

                    btn_notifications.setText("" + countNoitificationsView);

                } catch (Exception e) {
                    Log.i("ERRO NOTIFICATION", Objects.requireNonNull(e.getMessage()));
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void listarUsuariosRanking() {

        try {


            firebaseRef.child("usuarios").orderByChild("pontosG").limitToLast(100).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaRanking.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        UserModel usuario2Model = dados.getValue(UserModel.class);

                        listaRanking.add(usuario2Model);

                    }


                    try {

                        int tamanho = listaRanking.size();

                        String imagem = listaRanking.get(tamanho - 1).getImagem();
                        String imagem2 = listaRanking.get(tamanho - 2).getImagem();
                        String imagem3 = listaRanking.get(tamanho - 3).getImagem();


                        String pjogador = listaRanking.get(tamanho - 1).getPontosG() + " xp";
                        String pjogador2 = listaRanking.get(tamanho - 2).getPontosG() + " xp";
                        String pjogador3 = listaRanking.get(tamanho - 3).getPontosG() + " xp";


                        String[] jogador = listaRanking.get(tamanho - 1).getNome().split(" ");
                        String[] jogador2 = listaRanking.get(tamanho - 2).getNome().split(" ");
                        String[] jogador3 = listaRanking.get(tamanho - 3).getNome().split(" ");

                        Jogador.setText(jogador[0]);
                        Jogador2.setText(jogador2[0]);
                        Jogador3.setText(jogador3[0]);
                        pJogador.setText(pjogador);
                        pJogador2.setText(pjogador2);
                        pJogador3.setText(pjogador3);

                        if (imagem.isEmpty()) {
                            img_user.setImageResource(R.drawable.profile);
                        } else {

                            Glide
                                    .with(RankingFragment.this)
                                    .load(imagem)
                                    .centerCrop()
                                    .placeholder(R.drawable.profile)
                                    .into(img_user);
                        }

                        if (imagem2.isEmpty()) {
                            img_user2.setImageResource(R.drawable.profile);
                        } else {
                            Glide
                                    .with(RankingFragment.this)
                                    .load(imagem2)
                                    .centerCrop()
                                    .placeholder(R.drawable.profile)
                                    .into(img_user2);
                        }

                        if (imagem3.isEmpty()) {
                            img_user3.setImageResource(R.drawable.profile);
                        } else {
                            Glide
                                    .with(RankingFragment.this)
                                    .load(imagem3)
                                    .centerCrop()
                                    .placeholder(R.drawable.profile)
                                    .into(img_user3);
                        }


                    } catch (Exception e) {
                        img_user.setImageResource(R.drawable.profile);
                        img_user2.setImageResource(R.drawable.profile);
                        img_user3.setImageResource(R.drawable.profile);
                    }

                    //Abrir profileFriend

                    img_user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int tamanho = listaRanking.size();
                            UserModel usuarioSelecionado = listaRanking.get(tamanho - 1);
                            if (!usuarioSelecionado.getEmail().equals(user.getEmail())) {
                                Intent i = new Intent(getActivity(), FriendProfileActivity.class);
                                i.putExtra("id", usuarioSelecionado.getEmail());
                                startActivity(i);
                            }
                        }
                    });

                    img_user2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int tamanho = listaRanking.size();
                            UserModel usuarioSelecionado = listaRanking.get(tamanho - 2);
                            if (!usuarioSelecionado.getEmail().equals(user.getEmail())) {
                                Intent i = new Intent(getActivity(), FriendProfileActivity.class);
                                i.putExtra("id", usuarioSelecionado.getEmail());
                                startActivity(i);
                            }
                        }
                    });

                    img_user3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int tamanho = listaRanking.size();
                            UserModel usuarioSelecionado = listaRanking.get(tamanho - 3);
                            if (!usuarioSelecionado.getEmail().equals(user.getEmail())) {
                                Intent i = new Intent(getActivity(), FriendProfileActivity.class);
                                i.putExtra("id", usuarioSelecionado.getEmail());
                                startActivity(i);
                            }
                        }
                    });

                    adapterRanking.notifyDataSetChanged();

                    new Handler().postDelayed(new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            constraintPrincipal.setVisibility(View.VISIBLE);
                        }
                    }, 1000);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception ignored) {

        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        viewProfile();
        getAllNotifications();
        listarUsuariosRanking();
        super.onStart();
    }
}
