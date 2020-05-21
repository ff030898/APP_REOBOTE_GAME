package com.reobotetechnology.reobotegame.ui.feed;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.AmigosAdapters;
import com.reobotetechnology.reobotegame.adapter.FeedAdapters;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.PostModel;
import com.reobotetechnology.reobotegame.model.UsuarioModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FeedFragment extends Fragment {

    private FeedViewModel dashboardViewModel;
    RecyclerView recyclerAmigos, recyclerPost;
    private AmigosAdapters adapter;
    private FeedAdapters feedAdapters;
    private ArrayList<UsuarioModel> lista = new ArrayList<>();
    private ArrayList<PostModel> listaPost = new ArrayList<>();
    private ImageView imgAmei;
    private TextView textoAmei;
    VideoView videoview;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(FeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);



        //Configurações iniciais
        //recyclerAmigos = root.findViewById(R.id.recyclerStory);
        recyclerPost = root.findViewById(R.id.recyclerPost);


        //configurarAdapter
        //adapter = new AmigosAdapters(lista, getActivity());
        feedAdapters = new FeedAdapters(listaPost, getActivity());

        //RecyclerAmigos
        /*RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerAmigos.setLayoutManager(layoutManager);
        recyclerAmigos.setHasFixedSize(true);
        recyclerAmigos.setAdapter(adapter);*/

        //Recycler Post
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerPost.setLayoutManager(layoutManager2);
        recyclerPost.setHasFixedSize(true);
        recyclerPost.setAdapter(feedAdapters);


        int img = R.drawable.eu;
        int img6 = R.drawable.julia;
        int img2  = R.drawable.danilo;
        int img3 = R.drawable.isabela;
        int img4 = R.drawable.sabrina;
        int img5 = R.drawable.maria;


        int imgPost = R.drawable.img_feed;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
        Date data = new Date();
        String dataFormatada = formataData.format(data);


        UsuarioModel amigo = new UsuarioModel(1, "Fabrício", img, 1600, 1);
        PostModel p = new PostModel(1, img, 0, 0,0, amigo, "Postagem feita por mim para todos vocês verem", "", data);
        listaPost.add(p);

         amigo = new UsuarioModel(6, "Júlia", img6, 1550, 2);
        //lista.add(amigo);
        p = new PostModel(2, img6, 0, 0,0, amigo, "Postagem feita por mim para todos vocês verem a sogra do meu Varão.\n\n" + "Atos 16:31", "", data);
        listaPost.add(p);

        amigo = new UsuarioModel(3, "Danilo", img2, 1500, 3);
        //lista.add(amigo);
        p = new PostModel(3, img2, 0, 0,0, amigo, "Postagem feita por mim para todos vocês verem que o SELECT TÁ RODANDO KKKKK", "", data);
        listaPost.add(p);

        amigo = new UsuarioModel(2, "Isabela", img3, 1450, 4);
        //lista.add(amigo);
        p = new PostModel(4, img3, 0, 0,0, amigo, "Postagem feita por mim para todos vocês verem a minha roupa da PLANET. Só mina top usa. Desculpa invejosas eu canto mais que todas vocês kkkk", "", data);
        listaPost.add(p);

        amigo = new UsuarioModel(1, "Fabrício", img, 1600, 1);
        p = new PostModel(5, 0, 0, 0,0, amigo, "Postagem feita por mim para todos vocês verem", "", data);
        listaPost.add(p);

        amigo = new UsuarioModel(4, "Sabrina", img4, 1400, 5);
        //lista.add(amigo);
        p = new PostModel(6, img4, 0, 0,0, amigo, "Postagem feita por mim para todos vocês verem como eu estou princesa nessa foto. É uma pena que não quero casar com ngm kkkkkk", "", data);
        listaPost.add(p);

        amigo = new UsuarioModel(5, "Maria", img5, 1300, 6);
        p = new PostModel(7, img5, 0, 0,0, amigo, "Postagem feita por mim para todos vocês verem", "", data);
        listaPost.add(p);


        return root;
    }
}
