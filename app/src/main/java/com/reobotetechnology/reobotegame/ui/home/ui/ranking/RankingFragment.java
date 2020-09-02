package com.reobotetechnology.reobotegame.ui.home.ui.ranking;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.RankingAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.amigos.amigos_profile.Amigos_PerfilActivity;
import com.reobotetechnology.reobotegame.ui.perfil.PerfilActivity;


import java.util.ArrayList;
import java.util.Objects;

public class RankingFragment extends Fragment {

    private RecyclerView recyclerJogadores;
    private RankingAdapters adapterRanking;
    private ArrayList<UsuarioModel> listaRanking = new ArrayList<>();
    private ImageView img_user, img_user2, img_user3, img_user_logado;
    private TextView Jogador, Jogador2, Jogador3, pJogador, pJogador2, pJogador3, txtNomeLogado, txtPontosLogado, txtRankingLogado;
    private Button btnGeral;
    private Button btnSeguidores;
    private Button btnSeguindo;
    private Button btnHoje;
    private LinearLayout linearPrincipal;
    //private SwipeRefreshLayout swipeRefresh;
    private ConstraintLayout ConstraintUsuarioLogado;


    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();

    private ProgressBar progressBar5;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ranking, container, false);


        //swipeRefresh = root.findViewById(R.id.swipeRefresh);
        img_user = root.findViewById(R.id.img_user);
        img_user2 = root.findViewById(R.id.img_user2);
        img_user3 = root.findViewById(R.id.img_user3);
        Jogador = root.findViewById(R.id.Jogador);
        Jogador2 = root.findViewById(R.id.Jogador2);
        Jogador3 = root.findViewById(R.id.Jogador3);
        pJogador = root.findViewById(R.id.pJogador);
        pJogador2 = root.findViewById(R.id.pJogador2);
        pJogador3 = root.findViewById(R.id.pJogador3);
        View divider2 = root.findViewById(R.id.divider2);
        progressBar5 = root.findViewById(R.id.progressBar5);
        recyclerJogadores = root.findViewById(R.id.recyclerJogadores);
        linearPrincipal = root.findViewById(R.id.linearPrincipal);
        btnGeral = root.findViewById(R.id.btnGeral);
        btnSeguidores = root.findViewById(R.id.btnSeguidores);
        btnSeguindo = root.findViewById(R.id.btnSeguindo);
        img_user_logado = root.findViewById(R.id.img_user_logado);
        txtNomeLogado = root.findViewById(R.id.txtNomeLogado);
        txtPontosLogado = root.findViewById(R.id.txtPontosLogado);
        txtRankingLogado = root.findViewById(R.id.txtRankingLogado);
        txtNomeLogado = root.findViewById(R.id.txtNomeLogado);
        txtPontosLogado = root.findViewById(R.id.txtPontosLogado);
        txtRankingLogado = root.findViewById(R.id.txtRankingLogado);
        btnHoje = root.findViewById(R.id.btnHoje);
        ConstraintUsuarioLogado = root.findViewById(R.id.ConstraintUsuarioLogado);

        progressBar5.setVisibility(View.VISIBLE);

        linearPrincipal.setVisibility(View.GONE);
        ConstraintUsuarioLogado.setVisibility(View.GONE);

        btnGeral.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                listarUsuarios(0);
            }
        });

        btnSeguidores.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                listarUsuarios(1);
            }
        });

        btnSeguindo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                listarUsuarios(2);
            }
        });

        btnHoje.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                listarUsuarios(3);
            }
        });

        img_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tamanho = listaRanking.size();
                UsuarioModel usuarioSelecionado = listaRanking.get((tamanho - 1));
                if (usuarioSelecionado.getEmail().equals(user.getEmail())) {
                    startActivity(new Intent(getActivity(), PerfilActivity.class));
                } else {
                    Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
                    i.putExtra("id", usuarioSelecionado.getEmail());
                    startActivity(i);
                }
            }
        });

        img_user2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tamanho = listaRanking.size();
                UsuarioModel usuarioSelecionado = listaRanking.get((tamanho - 2));
                if (usuarioSelecionado.getEmail().equals(user.getEmail())) {
                    startActivity(new Intent(getActivity(), PerfilActivity.class));
                } else {
                    Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
                    i.putExtra("id", usuarioSelecionado.getEmail());
                    startActivity(i);
                }
            }
        });

        img_user3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tamanho = listaRanking.size();
                UsuarioModel usuarioSelecionado = listaRanking.get((tamanho - 3));

                if (usuarioSelecionado.getEmail().equals(user.getEmail())) {
                    startActivity(new Intent(getActivity(), PerfilActivity.class));
                } else {
                    Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
                    i.putExtra("id", usuarioSelecionado.getEmail());
                    startActivity(i);
                }
            }
        });

        img_user_logado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getActivity(), PerfilActivity.class));

            }
        });

        txtNomeLogado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PerfilActivity.class));
            }
        });

        //Eventos de Clique

        //Configurar evento de clique no recyclerview
        recyclerJogadores.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerJogadores,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                int tamanho = listaRanking.size();
                                UsuarioModel usuarioSelecionado = listaRanking.get((tamanho - position - 1));

                                if (usuarioSelecionado.getEmail().equals(user.getEmail())) {
                                    startActivity(new Intent(getActivity(), PerfilActivity.class));
                                } else {
                                    Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
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


        return root;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void listarUsuarios(final int tipo) {

        try {

            adapterRanking = new RankingAdapters(listaRanking, getActivity(), 0);

            //RecyclerRanking
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerJogadores.setLayoutManager(layoutManager);
            recyclerJogadores.setHasFixedSize(true);
            recyclerJogadores.setAdapter(adapterRanking);

            String select = "pontosG";

            if (tipo == 0) {
                select = "pontosG";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnGeral.setBackgroundTintList(ColorStateList.valueOf(0xFF0066cc));
                    btnGeral.setTextColor(ColorStateList.valueOf(0xFFffffff));
                    btnSeguindo.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnSeguindo.setTextColor(ColorStateList.valueOf(0xFF000000));
                    btnSeguidores.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnSeguidores.setTextColor(ColorStateList.valueOf(0xFF000000));
                    btnHoje.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnHoje.setTextColor(ColorStateList.valueOf(0xFF000000));

                }

            } else if (tipo == 1) {
                //select = "pontosM";

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnGeral.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnGeral.setTextColor(ColorStateList.valueOf(0xFF000000));
                    btnSeguindo.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnSeguindo.setTextColor(ColorStateList.valueOf(0xFF000000));
                    btnSeguidores.setBackgroundTintList(ColorStateList.valueOf(0xFF0066cc));
                    btnSeguidores.setTextColor(ColorStateList.valueOf(0xFFffffff));
                    btnHoje.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnHoje.setTextColor(ColorStateList.valueOf(0xFF000000));

                }
            } else if (tipo == 2) {
                //select = "pontosS";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnGeral.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnGeral.setTextColor(ColorStateList.valueOf(0xFF000000));
                    btnSeguindo.setBackgroundTintList(ColorStateList.valueOf(0xFF0066cc));
                    btnSeguindo.setTextColor(ColorStateList.valueOf(0xFFffffff));
                    btnSeguidores.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnSeguidores.setTextColor(ColorStateList.valueOf(0xFF000000));
                    btnHoje.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnHoje.setTextColor(ColorStateList.valueOf(0xFF000000));
                }

            } else if (tipo == 3) {
                select = "pontosD";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    btnGeral.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnGeral.setTextColor(ColorStateList.valueOf(0xFF000000));
                    btnSeguindo.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnSeguindo.setTextColor(ColorStateList.valueOf(0xFF000000));
                    btnSeguidores.setBackgroundTintList(ColorStateList.valueOf(0xFFf5f5f5));
                    btnSeguidores.setTextColor(ColorStateList.valueOf(0xFF000000));
                    btnHoje.setBackgroundTintList(ColorStateList.valueOf(0xFF0066cc));
                    btnHoje.setTextColor(ColorStateList.valueOf(0xFFffffff));
                }
            }

            firebaseRef.child("usuarios").orderByChild(select).addValueEventListener(new ValueEventListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaRanking.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        UsuarioModel usuario2Model = dados.getValue(UsuarioModel.class);

                        assert usuario2Model != null;
                        try {
                            if (usuario2Model.getEmail().equals(user.getEmail())) {
                                if (usuario2Model.getImagem().isEmpty()) {
                                    img_user_logado.setImageResource(R.drawable.user);
                                } else {

                                    Glide
                                            .with(RankingFragment.this)
                                            .load(usuario2Model.getImagem())
                                            .centerCrop()
                                            .placeholder(R.drawable.user)
                                            .into(img_user_logado);
                                }

                                txtNomeLogado.setText("Você");
                                txtPontosLogado.setText("" + usuario2Model.getPontosG());
                                txtRankingLogado.setText(usuario2Model.getRanking() + "º");
                            }

                        } catch (Exception e) {

                            img_user_logado.setImageResource(R.drawable.user);
                        }

                        listaRanking.add(usuario2Model);


                    }

                    adapterRanking.notifyDataSetChanged();

                    try {

                        int tamanho = listaRanking.size();

                        String imagem = listaRanking.get(tamanho - 1).getImagem();
                        String imagem2 = listaRanking.get(tamanho - 2).getImagem();
                        String imagem3 = listaRanking.get(tamanho - 3).getImagem();


                        String pjogador = listaRanking.get(tamanho - 1).getPontosG() + "";
                        String pjogador2 = listaRanking.get(tamanho - 2).getPontosG() + "";
                        String pjogador3 = listaRanking.get(tamanho - 3).getPontosG() + "";


                        String jogador[] = listaRanking.get(tamanho - 1).getNome().split(" ");
                        String jogador2[] = listaRanking.get(tamanho - 2).getNome().split(" ");
                        String jogador3[] = listaRanking.get(tamanho - 3).getNome().split(" ");

                        Jogador.setText(jogador[0]);
                        Jogador2.setText(jogador2[0]);
                        Jogador3.setText(jogador3[0]);
                        pJogador.setText(pjogador);
                        pJogador2.setText(pjogador2);
                        pJogador3.setText(pjogador3);

                        if (imagem.isEmpty()) {

                            img_user.setImageResource(R.drawable.user);
                        } else {

                            Glide
                                    .with(RankingFragment.this)
                                    .load(imagem)
                                    .centerCrop()
                                    .placeholder(R.drawable.user)
                                    .into(img_user);
                        }

                        if (imagem2.isEmpty()) {
                            img_user2.setImageResource(R.drawable.user);
                        } else {
                            Glide
                                    .with(RankingFragment.this)
                                    .load(imagem2)
                                    .centerCrop()
                                    .placeholder(R.drawable.user)
                                    .into(img_user2);
                        }

                        if (imagem3.isEmpty()) {
                            img_user3.setImageResource(R.drawable.user);
                        } else {
                            Glide
                                    .with(RankingFragment.this)
                                    .load(imagem3)
                                    .centerCrop()
                                    .placeholder(R.drawable.user)
                                    .into(img_user3);
                        }


                    } catch (Exception e) {
                        img_user.setImageResource(R.drawable.user);
                        img_user2.setImageResource(R.drawable.user);
                        img_user3.setImageResource(R.drawable.user);
                    }


                    progressBar5.setVisibility(View.GONE);
                    linearPrincipal.setVisibility(View.VISIBLE);
                    ConstraintUsuarioLogado.setVisibility(View.VISIBLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch(Exception e){

            Log.d("Erro", Objects.requireNonNull(e.getMessage()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        listarUsuarios(0);
    }
}
