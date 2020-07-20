package com.reobotetechnology.reobotegame.ui.home.ui.biblia;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.LivrosBibliaAdapters;
import com.reobotetechnology.reobotegame.adapter.PostAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.LivrosBibliaModel;
import com.reobotetechnology.reobotegame.model.PostModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.biblia_capitulos.CapitulosActivity;
import com.reobotetechnology.reobotegame.ui.main.WelcomeActivity;
import com.reobotetechnology.reobotegame.utils.LinearLayoutManagerWithSmoothScroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BibliaFragment extends Fragment {

    private List<LivrosBibliaModel> lista = new ArrayList<>();
    private List<LivrosBibliaModel> listaNovo = new ArrayList<>();
    private List<PostModel> postLista = new ArrayList<>();
    private LivrosBibliaAdapters adapter, adapter2;
    private PostAdapters adapterPost;
    private ProgressBar progressBar3;
    private LinearLayout LinearPrincipal;
    private SwipeRefreshLayout swipeRefresh;
    private int tamanho = 0;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private AdView mAdView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_biblia, container, false);

        mAdView = root.findViewById(R.id.adView);

        RecyclerView recyclerAntigoTestamento = root.findViewById(R.id.recyclerLivrosAntigo);
        RecyclerView recyclerNovoTestamento = root.findViewById(R.id.recyclerLivrosNovo);
        RecyclerView recyclerPost = root.findViewById(R.id.recyclerPost);
        progressBar3 = root.findViewById(R.id.progressBar3);
        LinearPrincipal = root.findViewById(R.id.LinearPrincipal);


        LinearPrincipal.setVisibility(View.GONE);
        progressBar3.setVisibility(View.VISIBLE);

        //configurarAdapter
        adapter = new LivrosBibliaAdapters(lista, getActivity());
        adapter2 = new LivrosBibliaAdapters(listaNovo, getActivity());

        //RecyclerLivros
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAntigoTestamento.setLayoutManager(layoutManager);
        recyclerAntigoTestamento.setHasFixedSize(true);
        recyclerAntigoTestamento.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerNovoTestamento.setLayoutManager(layoutManager2);
        recyclerNovoTestamento.setHasFixedSize(true);
        recyclerNovoTestamento.setAdapter(adapter2);

        adapterPost = new PostAdapters(postLista, getActivity());

        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerPost.setLayoutManager(layoutManager3);
        recyclerPost.setHasFixedSize(true);
        recyclerPost.setAdapter(adapterPost);


        //Configurar evento de clique no recyclerview
        recyclerAntigoTestamento.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerAntigoTestamento,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                tamanho = lista.size();

                                if (tamanho > 2) {

                                    LivrosBibliaModel livroSelecionado = lista.get(position);
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

        //Configurar evento de clique no recyclerview
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



        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void popularDevocional() {

        firebaseAuth = ConfiguracaoFireBase.getFirebaseAutenticacao();

        if (firebaseAuth.getCurrentUser() != null) {
            String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));

            databaseReference.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UsuarioModel user = dataSnapshot.getValue(UsuarioModel.class);
                    postLista.add(new PostModel(1, 0, 0, user, "", "PORQUE TEMOS QUE MORRER?", "Para algumas pessoas a morte pode parecer o fim. Porém a Bíblia Sagrada nos trás a certeza da vidá após a morte Como podemos ler no evangelho de", "hoje", "18:40"));
                    postLista.add(new PostModel(1, 0, 0, user, "", "Porque temos que morrer? O que acontece conosco?", "Para algumas pessoas a morte pode parecer o fim. Porém a Bíblia Sagrada nos trás a certeza da vidá após a morte Como podemos ler no evangelho de", "hoje", "18:40"));
                    postLista.add(new PostModel(1, 0, 0, user, "", "PORQUE TEMOS QUE MORRER?", "Para algumas pessoas a morte pode parecer o fim. Porém a Bíblia Sagrada nos trás a certeza da vidá após a morte Como podemos ler no evangelho de", "hoje", "18:40"));
                    postLista.add(new PostModel(1, 0, 0, user, "", "PORQUE TEMOS QUE MORRER?", "Para algumas pessoas a morte pode parecer o fim. Porém a Bíblia Sagrada nos trás a certeza da vidá após a morte Como podemos ler no evangelho de", "hoje", "18:40"));
                    adapterPost.notifyDataSetChanged();

                    progressBar3.setVisibility(View.GONE);
                    LinearPrincipal.setVisibility(View.VISIBLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }



    }

    private void popularLista() {

        lista.clear();
        listaNovo.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
        List<LivrosBibliaModel> lista2;
        lista2 = dataBaseAcess.listarAntigoTestamento();


        if (lista2.size() != 0) {
            lista.addAll(lista2);
        }

        List<LivrosBibliaModel> lista3;
        lista3 = dataBaseAcess.listarNovoTestamento();

        if (lista3.size() != 0) {
            listaNovo.addAll(lista3);
        }

        adapter.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();


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
        popularDevocional();
        popularLista();
    }
}
