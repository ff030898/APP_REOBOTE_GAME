package com.reobotetechnology.reobotegame.ui.ranking;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.RankingAdapters;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.UsuarioModel;
import com.reobotetechnology.reobotegame.ui.Amigos_PerfilActivity;

import java.util.ArrayList;

public class RankingFragment extends Fragment {

    private RecyclerView recyclerJogadores;
    private RankingAdapters adapterRanking;
    private ArrayList<UsuarioModel> listaRanking = new ArrayList<>();

    private RankingViewModel rankingViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rankingViewModel = ViewModelProviders.of(this).get(RankingViewModel.class);

        View root =  inflater.inflate(R.layout.ranking_fragment, container, false);

        recyclerJogadores = root.findViewById(R.id.recyclerJogadores);

        adapterRanking = new RankingAdapters(listaRanking, getActivity());

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerJogadores.setLayoutManager(layoutManager);
        recyclerJogadores.setHasFixedSize(true);
        recyclerJogadores.setAdapter(adapterRanking);


        int img = R.drawable.eu;
        int img2  = R.drawable.danilo;
        int img3 = R.drawable.isabela;
        int img4 = R.drawable.sabrina;
        int img5 = R.drawable.maria;
        int img6 = R.drawable.julia;


        UsuarioModel amigo = new UsuarioModel(1,"Fabrício", img, 1800, 1);

        listaRanking.add(amigo);

        amigo = new UsuarioModel(2,"Isabela", img3, 1600, 2);

        listaRanking.add(amigo);

        amigo = new UsuarioModel(3, "Danilo", img2, 1500, 3);

        listaRanking.add(amigo);

        amigo = new UsuarioModel(4, "Sabrina", img4, 1400, 4);

        listaRanking.add(amigo);

        amigo = new UsuarioModel(5, "Maria", img5, 1300, 5);

        listaRanking.add(amigo);

        amigo = new UsuarioModel(6, "Julia", img6, 1250, 6);
        listaRanking.add(amigo);


        for(int i = 0; i<=5; i++) {
            amigo = new UsuarioModel(1, "Fabrício", img, 1200, i+7);
            listaRanking.add(amigo);

        }


        //Eventos de Clique

        //Configurar evento de clique no recyclerview
        recyclerJogadores.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerJogadores,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                UsuarioModel usuarioSelecionado = listaRanking.get( position );
                                Intent i = new Intent(getActivity(), Amigos_PerfilActivity.class);
                                i.putExtra("id", usuarioSelecionado.getId());
                                i.putExtra("nome", usuarioSelecionado.getNome());
                                i.putExtra("imagem", usuarioSelecionado.getImg());
                                i.putExtra("ranking", usuarioSelecionado.getRanking());
                                startActivity( i );


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

}
