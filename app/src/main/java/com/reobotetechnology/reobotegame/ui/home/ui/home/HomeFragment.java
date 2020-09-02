package com.reobotetechnology.reobotegame.ui.home.ui.home;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.AmigosAdapters;
import com.reobotetechnology.reobotegame.adapter.LivrosBibliaAdapters;
import com.reobotetechnology.reobotegame.adapter.RankingAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.BibliaModel;
import com.reobotetechnology.reobotegame.model.LivrosBibliaModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.amigos.amigos_list.AmigosActivity;
import com.reobotetechnology.reobotegame.ui.biblia.biblia_livros.ListBibliaGrid;
import com.reobotetechnology.reobotegame.ui.biblia.biblia_toda.BibliaActivity;
import com.reobotetechnology.reobotegame.ui.biblia.biblia_capitulos.CapitulosActivity;
import com.reobotetechnology.reobotegame.ui.amigos.amigos_profile.Amigos_PerfilActivity;
import com.reobotetechnology.reobotegame.ui.partida.ModoPartidaActivity;
import com.reobotetechnology.reobotegame.ui.perfil.PerfilActivity;
import com.tapadoo.alerter.Alerter;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;


public class HomeFragment extends Fragment {

    //palavra do dia
    private int livro, capitulo, versiculo;
    private String nm_versiculo, nm_livro;
    //private TextView txtNomeUsuario;
    private TextView txtPalavra, txtVerso;
    private CircleImageView profileImage;
    private ProgressBar progressBar;
    private ConstraintLayout constraintPrincipal;

    //Configurações do banco de dados
    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfiguracaoFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    //Amigos Sugeridos
    private AmigosAdapters adapter;
    private ArrayList<UsuarioModel> lista = new ArrayList<>();
    private ImageView ic_amigos;

    //Biblia

    //A.T
    private LivrosBibliaAdapters adapterAntigo;
    private List<LivrosBibliaModel> listaAntigo = new ArrayList<>();
    private ImageView ic_antigo;

    //N.T
    private LivrosBibliaAdapters adapterNovo;
    private List<LivrosBibliaModel> listaNovo = new ArrayList<>();
    private ImageView ic_novo;
    private int tamanho = 0;

    //Top 5
    private RankingAdapters adapterRanking;
    private ArrayList<UsuarioModel> listaRanking = new ArrayList<>();


    //AdMob
    private AdView mAdView;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);


        mAdView = root.findViewById(R.id.adView);

        progressBar = root.findViewById(R.id.progressBar3);
        constraintPrincipal = root.findViewById(R.id.constraintPrincipal);
        profileImage = root.findViewById(R.id.img_user_logado);
        //txtNomeUsuario = root.findViewById(R.id.txtUsuario);
        final Button btnJogar = root.findViewById(R.id.btnJogar);
        ic_antigo = root.findViewById(R.id.ic_antigo);
        ic_novo = root.findViewById(R.id.ic_novo);
        ic_amigos = root.findViewById(R.id.ic_amigos);

        progressBar.setVisibility(View.VISIBLE);
        constraintPrincipal.setVisibility(View.GONE);

        //Ler na Biblia
        txtPalavra = root.findViewById(R.id.txtPalavra);
        txtVerso = root.findViewById(R.id.txtVerso);
        TextView lerNaBiblia = root.findViewById(R.id.lerNaBiblia);

        //PALAVRA DO DIA
        try {
            livro();
            capitulo();
            versiculo();
            txtPalavra.setText(nm_versiculo);
            String verso = nm_livro + " " + capitulo + ":" + versiculo;
            txtVerso.setText(verso);

        } catch (Exception e) {
            txtPalavra.setText(getString(R.string.versiculo));
            txtVerso.setText(getString(R.string.verso_cap_livro));
        }

        //Copiar e Compartilhar
        ImageView imgCopiar = root.findViewById(R.id.imgCopiar);
        ImageView imgCompartilhar = root.findViewById(R.id.imgCompartilhar);

        imgCopiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (livro != 0) {
                    ClipboardManager clipboardManager = (ClipboardManager) requireActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("Versiculos", txtPalavra.getText() + "\n\n" + txtVerso.getText());
                    assert clipboardManager != null;
                    clipboardManager.setPrimaryClip(clipData);

                    Alerter.create(getActivity())
                            .setTitle("Obaa...")
                            .setText("Texto copiado para a área de transferência!")
                            .setIcon(R.drawable.ic_success)
                            .setDuration(2000)
                            .setBackgroundColorRes(R.color.colorGreen1)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Alerter.hide();
                                }
                            })
                            .show();

                }
            }
        });


        imgCompartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent share = new Intent(android.content.Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                share.putExtra(Intent.EXTRA_TEXT,
                        txtPalavra.getText() + "\n\n" + txtVerso.getText());

                startActivity(Intent.createChooser(share, "Compartilhar"));
            }
        });

        btnJogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ModoPartidaActivity.class));
            }
        });

        lerNaBiblia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (livro != 0) {
                    Intent i = new Intent(getActivity(), BibliaActivity.class);
                    i.putExtra("nm_livro", nm_livro);
                    i.putExtra("livroSelecionado", livro);
                    i.putExtra("capitulo", capitulo);
                    i.putExtra("versiculo", versiculo);
                    startActivity(i);
                }
            }
        });

        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), PerfilActivity.class);
                startActivity(i);
            }
        });

        ic_amigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AmigosActivity.class));
            }
        });

        ic_antigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListBibliaGrid.class);
                i.putExtra("cd_testamento", 0);
                startActivity( i );
            }
        });

        ic_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ListBibliaGrid.class);
                i.putExtra("cd_testamento", 1);
                startActivity( i );
            }
        });

        //Configurações Recycler
        RecyclerView recyclerAmigos = root.findViewById(R.id.recyclerAmigos);
        RecyclerView recyclerRanking = root.findViewById(R.id.recyclerRanking);
        RecyclerView recyclerAntigoTestamento = root.findViewById(R.id.recyclerLivrosAntigo);
        RecyclerView recyclerNovoTestamento = root.findViewById(R.id.recyclerLivrosNovo);

        //configurarAdapter
        adapter = new AmigosAdapters(lista, getActivity());
        adapterAntigo = new LivrosBibliaAdapters(listaAntigo, getActivity());
        adapterNovo = new LivrosBibliaAdapters(listaNovo, getActivity());
        adapterRanking = new RankingAdapters(listaRanking, getActivity(), 1);

        //Configuraçoes de Layout do Recycler

        //RecyclerAmigos
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAmigos.setLayoutManager(layoutManager);
        recyclerAmigos.setHasFixedSize(true);
        recyclerAmigos.setAdapter(adapter);

        //RecyclerLivros
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAntigoTestamento.setLayoutManager(layoutManager1);
        recyclerAntigoTestamento.setHasFixedSize(true);
        recyclerAntigoTestamento.setAdapter(adapterAntigo);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerNovoTestamento.setLayoutManager(layoutManager2);
        recyclerNovoTestamento.setHasFixedSize(true);
        recyclerNovoTestamento.setAdapter(adapterNovo);

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getActivity());
        recyclerRanking.setLayoutManager(layoutManager4);
        recyclerRanking.setHasFixedSize(true);
        recyclerRanking.setAdapter(adapterRanking);

        //Eventos de Clique

        //Amigos Sugeridos
        recyclerAmigos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerAmigos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                UsuarioModel usuarioSelecionado = lista.get(position);
                                Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
                                i.putExtra("id", usuarioSelecionado.getEmail());
                                startActivity(i);

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


        //Recycler Antigo
        recyclerAntigoTestamento.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerAntigoTestamento,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                tamanho = listaAntigo.size();

                                if (tamanho > 2) {

                                    LivrosBibliaModel livroSelecionado = listaAntigo.get(position);
                                    Intent i = new Intent(getActivity(), CapitulosActivity.class);
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

        //Recycler Novo
        recyclerNovoTestamento.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerNovoTestamento,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                tamanho = listaNovo.size();

                                if (tamanho > 2) {

                                    LivrosBibliaModel livroSelecionado = listaNovo.get(position);
                                    Intent i = new Intent(getActivity(), CapitulosActivity.class);
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

        //Recycler Ranking
        recyclerRanking.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerRanking,
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



    //Usuario Logado
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperaUsuario() {

        if (autenticacao.getCurrentUser() != null) {

            //txtNomeUsuario.setText(user.getDisplayName());

            try {
                if (user.getPhotoUrl() == null) {
                    Glide
                            .with(HomeFragment.this)
                            .load(R.drawable.user)
                            .centerCrop()
                            .placeholder(R.drawable.user)
                            .into(profileImage);
                } else {
                    Glide
                            .with(HomeFragment.this)
                            .load(user.getPhotoUrl())
                            .centerCrop()
                            .placeholder(R.drawable.user)
                            .into(profileImage);
                }
            } catch (Exception e) {
                Glide
                        .with(HomeFragment.this)
                        .load(R.drawable.user)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(profileImage);

            }

        }


    }

    //Palavra do dia
    private void livro() {

        try {
            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
            List<LivrosBibliaModel> livroLista = dataBaseAcess.listarLivroPalavra();
            LivrosBibliaModel p = livroLista.get(0);
            if (p.getId() != 0) {
                livro = p.getId();
                nm_livro = p.getNome();
            } else {
                livro = 0;
                nm_livro = "ERRO";
            }
        } catch (Exception e) {
            livro = 0;
            nm_livro = "ERRO";
        }

    }

    private void capitulo() {

        try {

            if (livro != 0) {
                DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
                capitulo = dataBaseAcess.capituloPalavra(livro);
            } else {
                capitulo = 0;
            }
        } catch (Exception e) {
            capitulo = 0;
        }


    }

    private void versiculo() {

        try {

            if (livro != 0) {
                DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
                List<BibliaModel> versiculoLista = dataBaseAcess.listarVersiculoPalavra(livro, capitulo);
                BibliaModel v = versiculoLista.get(0);
                versiculo = v.getVerso();
                nm_versiculo = v.getText();

            } else {
                versiculo = 0;
                nm_livro = "ERRO";
            }

        } catch (Exception e) {
            versiculo = 0;
            nm_livro = "ERRO";
        }

    }



    //ListasRecycler
    private void listarAmigos() {

        firebaseRef.child("usuarios").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                lista.clear();

                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    try {
                        UsuarioModel usuario2Model = dados.getValue(UsuarioModel.class);

                        assert usuario2Model != null;
                        if (!usuario2Model.getEmail().equals(user.getEmail())) {
                            lista.add(usuario2Model);
                        }
                    }catch(Exception e){
                        Log.i("ERRO: ", Objects.requireNonNull(e.getMessage()));
                    }

                }

                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void listarLivros() {

        listaAntigo.clear();
        listaNovo.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
        List<LivrosBibliaModel> lista2;
        lista2 = dataBaseAcess.listarAntigoTestamento();


        if (lista2.size() != 0) {
            listaAntigo.addAll(lista2);
        }

        List<LivrosBibliaModel> lista3;
        lista3 = dataBaseAcess.listarNovoTestamento();

        if (lista3.size() != 0) {
            listaNovo.addAll(lista3);
        }

        adapterAntigo.notifyDataSetChanged();
        adapterNovo.notifyDataSetChanged();


    }

    private void atualizarRanking(){
        final List<UsuarioModel> listaRankingAll = new ArrayList<>();


        try {

            firebaseRef.child("usuarios").orderByChild("pontosG").addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaRankingAll.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        UsuarioModel usuario2Model = dados.getValue(UsuarioModel.class);

                        listaRankingAll.add(usuario2Model);
                    }

                    int tamanho = listaRankingAll.size();

                    for(int i=0; i<tamanho; i++){
                        int pos = (i + 1);
                        UsuarioModel usuarioSelecionado = listaRankingAll.get((tamanho - i - 1));
                        String idUsuario = Base64Custom.codificarBase64((Objects.requireNonNull(usuarioSelecionado.getEmail())));
                        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);
                        usuarioRef.child("ranking").setValue(pos);
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception ignored){

        }
    }

    private void listarUsuariosRanking() {

        try {

            firebaseRef.child("usuarios").orderByChild("pontosD").limitToLast(5).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    listaRanking.clear();
                    for (DataSnapshot dados : dataSnapshot.getChildren()) {

                        UsuarioModel usuario2Model = dados.getValue(UsuarioModel.class);

                        listaRanking.add(usuario2Model);

                    }

                    adapterRanking.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    constraintPrincipal.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception ignored){

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

            }
        }, 2000);

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        recuperaUsuario();
        listarAmigos();
        listarLivros();
        atualizarRanking();
        listarUsuariosRanking();
    }


}
