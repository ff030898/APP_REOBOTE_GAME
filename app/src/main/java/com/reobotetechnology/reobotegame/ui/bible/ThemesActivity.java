package com.reobotetechnology.reobotegame.ui.bible;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import com.reobotetechnology.reobotegame.adapter.BibleAdapters;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.VersesBibleModel;
import com.reobotetechnology.reobotegame.model.ThemeslistModel;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

public class ThemesActivity extends AppCompatActivity {

    private RecyclerView recyclerThemesVerses;
    private BibleAdapters adapter;
    private List<VersesBibleModel> listVerses = new ArrayList<>();

    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back;


    private CoordinatorLayout constraintPrincipal;

    //Params
    String theme;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);

        progressBar = findViewById(R.id.progressBar3);
        constraintPrincipal = findViewById(R.id.constraintPrincipal);
        constraintPrincipal.setVisibility(View.GONE);

        recyclerThemesVerses = findViewById(R.id.recyclerThemesVerses);

        //configurarAdapter
        adapter = new BibleAdapters(listVerses, getApplicationContext(), 1);

        //RecyclerThemes
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerThemesVerses.setLayoutManager(layoutManager);
        recyclerThemesVerses.setHasFixedSize(true);

        recyclerThemesVerses.setAdapter(adapter);

        //Vem da Activity Principal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            theme = extras.getString("theme");

            if (theme != null) {
                this.listVersesofTheme(theme);
            }

        }

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(theme);
        txt_subtitle.setText("Versículos sobre '"+theme+"'");

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

        //ThemesVerse
        recyclerThemesVerses.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerThemesVerses,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                VersesBibleModel themes = listVerses.get(position);

                                DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
                                String book = dataBaseAcess.findBook(themes.getLivro());

                                Intent i = new Intent(getApplicationContext(), BiblieActivity.class);
                                i.putExtra("nm_livro", book);
                                i.putExtra("livroSelecionado",themes.getLivro());
                                i.putExtra("capitulo",  themes.getCapitulo());
                                i.putExtra("versiculo", themes.getVerso());
                                startActivity( i );

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                VersesBibleModel themes = listVerses.get(position);

                                DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
                                String book = dataBaseAcess.findBook(themes.getLivro());

                                ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("Versiculos", themes.getText() + "\n\n" + book+" "+themes.getCapitulo()+":"+themes.getVerso());
                                assert clipboardManager != null;
                                clipboardManager.setPrimaryClip(clipData);

                                Alerter.create(ThemesActivity.this)
                                        .setTitle("Obaa...")
                                        .setText("Texto copiado para a área de transferência!")
                                        .setIcon(R.drawable.ic_success)
                                        .setDuration(2000)
                                        .setBackgroundColorRes(R.color.colorGreen1)
                                        .setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Alerter.hide();
                                            }
                                        })
                                        .show();


                            }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            }
                        }
                )
        );
    }


    private void listVersesofTheme(String theme) {

        listVerses.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

        List<VersesBibleModel> lista2 = new ArrayList<>();

        List<ThemeslistModel> listThemes = new ArrayList<>();
        listThemes = dataBaseAcess.listThemesVerses(theme);

        for (int i = 0; i < listThemes.size(); i++) {

            lista2 = dataBaseAcess.findThemes(listThemes.get(i).getBook_id(), listThemes.get(i).getChapter_id(), listThemes.get(i).getVerse_id());
            listVerses.addAll(lista2);
        }

        adapter.notifyDataSetChanged();
    }


}
