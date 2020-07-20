package com.reobotetechnology.reobotegame.ui.home.ui.blog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.PostAdapters;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.PostModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.blog.NewPostActivity;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class BlogFragment extends Fragment {

    private BlogViewModel blogViewModel;

    private List<PostModel> postLista = new ArrayList<>();
    private PostAdapters adapter;

    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = firebaseAuth.getCurrentUser();
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference();

    private ProgressBar progresso;
    private FrameLayout framePrincipal;
    private LinearLayout linearPosts;
    private FloatingActionButton menu;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        View root = inflater.inflate(R.layout.fragment_blog, container, false);


        RecyclerView recyclerPost = root.findViewById(R.id.recyclerPost);


        progresso = root.findViewById(R.id.progresso);
        menu = root.findViewById(R.id.menu);
        framePrincipal = root.findViewById(R.id.framePrincipal);
        linearPosts = root.findViewById(R.id.linearPost);
        adapter = new PostAdapters(postLista, getActivity());


        //RecyclerPost
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerPost.setLayoutManager(layoutManager);
        recyclerPost.setHasFixedSize(true);
        recyclerPost.setAdapter(adapter);

        progresso.setVisibility(View.VISIBLE);
        framePrincipal.setVisibility(View.GONE);
        linearPosts.setVisibility(View.GONE);
        menu.setVisibility(View.GONE);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                novoPost();
            }
        });


        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void recuperarPost(){

        firebaseAuth = ConfiguracaoFireBase.getFirebaseAutenticacao();

        if (firebaseAuth.getCurrentUser() != null) {
            String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));

            databaseReference.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    UsuarioModel user = dataSnapshot.getValue(UsuarioModel.class);
                    postLista.add(new PostModel(1, 0, 1, user, "", "RICK AND MARTY - EPISÓDIO 1", "1ª TEMPORADA", "hoje", "18:40"));
                    postLista.add(new PostModel(1, 0, 1, user, "", "RICK AND MARTY - EPISÓDIO 1", "1ª TEMPORADA", "hoje", "18:40"));
                    postLista.add(new PostModel(1, 0, 1, user, "", "RICK AND MARTY - EPISÓDIO 1", "1ª TEMPORADA", "hoje", "18:40"));
                    postLista.add(new PostModel(1, 0, 1, user, "", "RICK AND MARTY - EPISÓDIO 1", "1ª TEMPORADA", "hoje", "18:40"));
                    postLista.add(new PostModel(1, 0, 1, user, "", "RICK AND MARTY - EPISÓDIO 1", "1ª TEMPORADA", "hoje", "18:40"));
                    postLista.add(new PostModel(1, 0, 1, user, "", "RICK AND MARTY - EPISÓDIO 1", "1ª TEMPORADA", "hoje", "18:40"));
                    adapter.notifyDataSetChanged();


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

                progresso.setVisibility(View.GONE);
                framePrincipal.setVisibility(View.VISIBLE);
                linearPosts.setVisibility(View.VISIBLE);
                menu.setVisibility(View.VISIBLE);

    }



    private void novoPost(){
        startActivity(new Intent(getActivity(), NewPostActivity.class));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onStart() {
        super.onStart();
        recuperarPost();
    }
}
