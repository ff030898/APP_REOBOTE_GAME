package com.reobotetechnology.reobotegame.ui.biblia.biblia_livros;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.LivrosBibliaAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.LivrosBibliaModel;
import com.reobotetechnology.reobotegame.ui.biblia.biblia_capitulos.CapitulosActivity;

import java.util.ArrayList;
import java.util.List;


public class ListBibliaGrid extends AppCompatActivity {

    //BIBLIA
    private LivrosBibliaAdapters adapter;
    private List<LivrosBibliaModel> lista = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_biblia_grid);

        //COMPONENTS
        final EditText editPesquisarLivros = findViewById(R.id.editPesquisarLivros);
        final ProgressBar progressBar = findViewById(R.id.progressBar);
        //Configurando Recycler
        final RecyclerView recyclerBiblia = findViewById(R.id.recyclerBiblia);

        editPesquisarLivros.setVisibility(View.GONE);
        recyclerBiblia.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        //Configurando Adapter
        adapter = new LivrosBibliaAdapters(lista, getApplicationContext());

        //RecyclerLivros
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerBiblia.setLayoutManager(layoutManager);
        recyclerBiblia.setHasFixedSize(true);
        recyclerBiblia.setAdapter(adapter);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                editPesquisarLivros.setVisibility(View.VISIBLE);
                recyclerBiblia.setVisibility(View.VISIBLE);


            }
        }, 2000);

        //Vem da Activity Principal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int cd_testamento = extras.getInt("cd_testamento");
                listarLivros(cd_testamento);
        }


        //Recycler Antigo
        recyclerBiblia.addOnItemTouchListener(
                new RecyclerItemClickListener(
                       getApplicationContext(),
                        recyclerBiblia,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                int tamanho = lista.size();

                                if (tamanho > 2) {

                                    LivrosBibliaModel livroSelecionado = lista.get(position);
                                    Intent i = new Intent(getApplicationContext(), CapitulosActivity.class);
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



    }

    private void listarLivros(int cd_testamento) {

        lista.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        List<LivrosBibliaModel> lista2;

        if(cd_testamento == 0) {
            lista2 = dataBaseAcess.listarAntigoTestamento();
        }else{
            lista2 = dataBaseAcess.listarNovoTestamento();
        }

        if (lista2.size() != 0) {
            lista.addAll(lista2);
        }


        adapter.notifyDataSetChanged();

    }
}
