package com.reobotetechnology.reobotegame.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.AmigosPesquisarAdapters;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.UsuarioModel;

import java.util.ArrayList;
import java.util.List;

public class AmigosActivity extends AppCompatActivity {

    RecyclerView recyclerAmigosPesquisar;
    AmigosPesquisarAdapters adapter;
    List<UsuarioModel> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);
        lista = new ArrayList<>();

        recyclerAmigosPesquisar = findViewById(R.id.recyclerAmigosPesquisar);

        adapter = new AmigosPesquisarAdapters(lista, getApplicationContext());

        //RecyclerRanking
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerAmigosPesquisar.setLayoutManager(layoutManager);
        recyclerAmigosPesquisar.setHasFixedSize(true);
        recyclerAmigosPesquisar.setAdapter(adapter);

        getSupportActionBar().setTitle("Procurar Amigos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int img = R.drawable.eu;
        int img2  = R.drawable.danilo;
        int img3 = R.drawable.isabela;
        int img4 = R.drawable.sabrina;
        int img5 = R.drawable.maria;
        int img6 = R.drawable.julia;


        UsuarioModel amigo = new UsuarioModel(1,"Fabrício", img, 1800, 1);
        lista.add(amigo);


        amigo = new UsuarioModel(2,"Isabela", img3, 1600, 2);
        lista.add(amigo);


        amigo = new UsuarioModel(3, "Danilo", img2, 1500, 3);
        lista.add(amigo);


        amigo = new UsuarioModel(4, "Sabrina", img4, 1400, 4);
        lista.add(amigo);


        amigo = new UsuarioModel(5, "Maria", img5, 1300, 5);
        lista.add(amigo);


        amigo = new UsuarioModel(6, "Julia", img6, 1250, 6);
        lista.add(amigo);


        for(int i = 0; i<=5; i++) {
            amigo = new UsuarioModel(1, "Fabrício "+(i+1), img, 1200, i+7);
            lista.add(amigo);

        }


        //Configurar evento de clique no recyclerview
        recyclerAmigosPesquisar.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerAmigosPesquisar,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                UsuarioModel usuarioSelecionado = lista.get( position );
                                Intent i = new Intent(getApplicationContext(), Amigos_PerfilActivity.class);
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




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ver_todos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.menu_ver_todos_amigos:
                startActivity(new Intent(getApplicationContext(), AmigosActivity.class));
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
