package com.reobotetechnology.reobotegame.ui.bible;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import android.widget.Toast;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.ListBooksOfBibleAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.BooksOfBibleModel;

import java.util.ArrayList;
import java.util.List;


public class ListBiblieScreen extends AppCompatActivity {

    //BIBLIA
    private ListBooksOfBibleAdapters adapter;
    private List<BooksOfBibleModel> lista = new ArrayList<>();

    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back;


    private CoordinatorLayout constraintPrincipal;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_biblia_screen);


        progressBar = findViewById(R.id.progressBar);
        constraintPrincipal = findViewById(R.id.constraintPrincipal);
        constraintPrincipal.setVisibility(View.GONE);

        //Configurando Recycler
        final RecyclerView recyclerBiblia = findViewById(R.id.recyclerBiblia);

        progressBar.setVisibility(View.VISIBLE);

        //Configurando Adapter
        adapter = new ListBooksOfBibleAdapters(lista, getApplicationContext());

        //RecyclerLivros  //RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerBiblia.setLayoutManager(layoutManager);
        recyclerBiblia.setAdapter(adapter);

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        //Vem da Activity Principal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int cd_testamento = extras.getInt("cd_testamento");
            listarLivros(cd_testamento);
            if(cd_testamento == 0) {
                txt_title.setText(getString(R.string.antigo_testamento));
            }else {
                txt_title.setText(getString(R.string.novo_testamento));
            }
            txt_subtitle.setText(getString(R.string.escolha_um_livro_da_b_blia_abaixo_para_meditar));
        }


        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                constraintPrincipal.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);


            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


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

                                    BooksOfBibleModel livroSelecionado = lista.get(position);
                                    Intent i = new Intent(getApplicationContext(), ChaptersActivity.class);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void listarLivros(int cd_testamento) {

        lista.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
        List<BooksOfBibleModel> lista2;



        if(cd_testamento == 0) {
            lista2 = dataBaseAcess.listarAntigoTestamento();

        }else{
            lista2 = dataBaseAcess.listarNovoTestamento();
        }

        if (lista2.size() != 0) {
            for(int i = 0; i<lista2.size(); i++) {
                int learningBook = dataBaseAcess.learningBook(lista2.get(i).getId());
                boolean favorited = dataBaseAcess.favorited(lista2.get(i).getId());
                lista2.get(i).setLearning(learningBook);
                lista2.get(i).setFavorited(favorited);
                lista.add(lista2.get(i));
            }
        }


        adapter.notifyDataSetChanged();

    }
}
