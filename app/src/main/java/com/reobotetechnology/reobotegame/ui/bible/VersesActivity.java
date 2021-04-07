package com.reobotetechnology.reobotegame.ui.bible;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.VersesRectangleAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.CaptherOfVersesNBibleModel;
import com.reobotetechnology.reobotegame.ui.bible.BiblieActivity;

import java.util.ArrayList;
import java.util.List;


public class VersesActivity extends AppCompatActivity {


    private RecyclerView recyclerVersiculos;
    private VersesRectangleAdapters adapter;
    private List<CaptherOfVersesNBibleModel> lista;

    private CoordinatorLayout constraintPrincipal;
    private ProgressBar progressBar;

    private int livro, capitulo, versiculos;
    private String nm_livro;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back;



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versiculos);

        progressBar = findViewById(R.id.progressBar3);
        constraintPrincipal = findViewById(R.id.constraintPrincipal);
        constraintPrincipal.setVisibility(View.GONE);

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


        String cap = nm_livro+" "+capitulo;

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(cap);
        txt_subtitle.setText(getString(R.string.verse_description));

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);


        recyclerVersiculos = findViewById(R.id.recyclerVersiculos);

        //configurarAdapter
        adapter = new VersesRectangleAdapters(lista, getApplicationContext());
        //RecyclerAmigos
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 5);
        recyclerVersiculos.setLayoutManager(layoutManager);
        recyclerVersiculos.setHasFixedSize(true);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                constraintPrincipal.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);
                recyclerVersiculos.setAdapter(adapter);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //Configurar evento de clique no recyclerview
        recyclerVersiculos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerVersiculos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Intent i = new Intent(getApplicationContext(), BiblieActivity.class);
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

            CaptherOfVersesNBibleModel c = new CaptherOfVersesNBibleModel(i+1,i+1);
            lista.add(c);
        }


    }


}
