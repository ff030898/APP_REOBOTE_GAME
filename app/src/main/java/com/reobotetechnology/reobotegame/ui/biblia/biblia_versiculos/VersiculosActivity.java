package com.reobotetechnology.reobotegame.ui.biblia.biblia_versiculos;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.CapVersosAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.CapVersosModel;
import com.reobotetechnology.reobotegame.ui.biblia.biblia_toda.BibliaActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VersiculosActivity extends AppCompatActivity {


    RecyclerView recyclerVersiculos;
    CapVersosAdapters adapter;
    List<CapVersosModel> lista;

    ProgressBar progressBar;

    int livro, capitulo, versiculos;
    String nm_livro;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versiculos);

        progressBar = findViewById(R.id.progressBar3);

        lista = new ArrayList<>();

        //Vem da Activity Principal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            livro = extras.getInt("livroSelecionado");
            nm_livro = extras.getString("nm_livro");
            capitulo = extras.getInt("capitulo");

            if(livro != 0){
                versiculos();
            }

        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(nm_livro+" "+capitulo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerVersiculos = findViewById(R.id.recyclerVersiculos);

        //configurarAdapter
        adapter = new CapVersosAdapters(lista, getApplicationContext());
        //RecyclerAmigos
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 5);
        recyclerVersiculos.setLayoutManager(layoutManager);
        recyclerVersiculos.setHasFixedSize(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                recyclerVersiculos.setVisibility(View.VISIBLE);
                recyclerVersiculos.setAdapter(adapter);

            }
        }, 2000);


        //Configurar evento de clique no recyclerview
        recyclerVersiculos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerVersiculos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                CapVersosModel livroSelecionado = lista.get( position );
                                Intent i = new Intent(getApplicationContext(), BibliaActivity.class);
                                i.putExtra("nm_livro", nm_livro);
                                i.putExtra("livroSelecionado",livro);
                                i.putExtra("capitulo",  capitulo);
                                i.putExtra("versiculo", position+1);
                                startActivity( i );

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                //alert("Texto copiado para area de transferência");
                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );

    }

    private void versiculos(){

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());


        versiculos = dataBaseAcess.listarVersosPalavra(livro, capitulo);
        //Toast.makeText(getApplicationContext(), "Versos: "+versiculos, Toast.LENGTH_SHORT).show();

        for (int i=0; i<versiculos; i++){

            CapVersosModel c = new CapVersosModel(i+1,i+1);
            lista.add(c);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sair, menu);

        return true;
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.menu_sair:
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
