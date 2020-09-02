package com.reobotetechnology.reobotegame.ui.home.ui.biblia;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;


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
import com.reobotetechnology.reobotegame.adapter.CategoriasAdapters;
import com.reobotetechnology.reobotegame.adapter.PostAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.CategoriaModel;
import com.reobotetechnology.reobotegame.model.PostModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BibliaFragment extends Fragment {


    private List<PostModel> postLista = new ArrayList<>();
    private List<CategoriaModel> categoriaLista = new ArrayList<>();
    private PostAdapters adapterPost;
    private CategoriasAdapters adapterCategoria;
    private ProgressBar progressBar3;
    private ConstraintLayout ConstraintPrincipal;
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

        RecyclerView recyclerPost = root.findViewById(R.id.recyclerPost);
        RecyclerView recyclerCategoria = root.findViewById(R.id.recyclerCategorias);
        progressBar3 = root.findViewById(R.id.progressBar3);
        ConstraintPrincipal = root.findViewById(R.id.ConstraintPrincipal);


        ConstraintPrincipal.setVisibility(View.GONE);
        progressBar3.setVisibility(View.VISIBLE);

        //configurarAdapter

        adapterCategoria = new CategoriasAdapters(categoriaLista, getActivity());

        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerCategoria.setLayoutManager(layoutManager4);
        recyclerCategoria.setHasFixedSize(true);
        recyclerCategoria.setAdapter(adapterCategoria);

        adapterPost = new PostAdapters(postLista, getActivity());

        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        recyclerPost.setLayoutManager(layoutManager3);
        recyclerPost.setHasFixedSize(true);
        recyclerPost.setAdapter(adapterPost);

        return root;
    }

    private void popularCategorias(){

        categoriaLista.add(new CategoriaModel(1,"TODOS"));
        categoriaLista.add(new CategoriaModel(2,"CRIANÇAS"));
        categoriaLista.add(new CategoriaModel(3,"JOVENS"));
        categoriaLista.add(new CategoriaModel(4,"HOMENS"));
        categoriaLista.add(new CategoriaModel(5,"MULHERES"));
        adapterCategoria.notifyDataSetChanged();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void popularPosts() {

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
                    ConstraintPrincipal.setVisibility(View.VISIBLE);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    AdRequest adRequest = new AdRequest.Builder().build();
                    mAdView.loadAd(adRequest);

                }
            }, 2000);


        }



    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        popularCategorias();
        popularPosts();

    }
}
