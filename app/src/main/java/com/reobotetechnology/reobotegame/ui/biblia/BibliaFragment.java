package com.reobotetechnology.reobotegame.ui.biblia;

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
import android.widget.Toast;


import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.BibliaAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.BibliaModel;
import com.reobotetechnology.reobotegame.ui.CapitulosActivity;

import java.util.ArrayList;
import java.util.List;


public class BibliaFragment extends Fragment {

    private BibliaViewModel mViewModel;
    private List<BibliaModel> lista = new ArrayList<>();
    private BibliaAdapters adapter;
    private RecyclerView recyclerLivros;
    int tamanho = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                ViewModelProviders.of(this).get(BibliaViewModel.class);
        View root = inflater.inflate(R.layout.biblia_fragment, container, false);


        recyclerLivros = root.findViewById(R.id.recyclerLivros);

        //configurarAdapter
        adapter = new BibliaAdapters(lista, getActivity());


        //RecyclerAmigos
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerLivros.setLayoutManager(layoutManager);
        recyclerLivros.setHasFixedSize(true);
        recyclerLivros.setAdapter(adapter);

        popularLista();


        //Eventos de Clique

        //Configurar evento de clique no recyclerview
        recyclerLivros.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(),
                        recyclerLivros,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                tamanho = lista.size();

                                if(tamanho > 2) {

                                    BibliaModel livroSelecionado = lista.get(position);
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


    private void popularLista() {

        try {
            lista.clear();
            DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getActivity());
            List<BibliaModel> lista2 = new ArrayList<>();
            lista2 = dataBaseAcess.listarLivros();

            if (lista2.size() != 0) {
                lista.addAll(lista2);

            } else {
                lista.add(new BibliaModel(1, 1, "ERRO AO CARREGAR BANCO DE DADOS DA BÍBLIA"));
                Toast.makeText(getActivity(), "ERRO AO CARREGAR LIVROS BÍBLIA", Toast.LENGTH_SHORT).show();
            }

            adapter.notifyDataSetChanged();
        }catch (Exception e){

            lista.add(new BibliaModel(1, 1, "ERRO AO CARREGAR BANCO DE DADOS DA BÍBLIA"));
            Toast.makeText(getActivity(), "ERRO AO CARREGAR BANCO DE DADOS DA BÍBLIA", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    }



}
