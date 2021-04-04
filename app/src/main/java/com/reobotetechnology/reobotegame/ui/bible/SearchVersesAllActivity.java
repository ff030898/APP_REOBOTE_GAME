package com.reobotetechnology.reobotegame.ui.bible;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.reobotetechnology.reobotegame.ui.bible.biblia_toda.BibliaActivity;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SearchVersesAllActivity extends AppCompatActivity {

    private RecyclerView recyclerThemesVerses;
    private BibleAdapters adapter;
    private List<VersesBibleModel> listVerses = new ArrayList<>();

    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back;


    private CoordinatorLayout constraintPrincipal;


    private AppCompatEditText inputSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_verses_all);

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


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.bible));
        txt_subtitle.setText(getString(R.string.searchAll));

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

        listVersesofTheme("");

        inputSearch = findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void afterTextChanged(Editable s) {
                String text = Objects.requireNonNull(inputSearch.getText()).toString();
                listVersesofTheme(text);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

                                Intent i = new Intent(getApplicationContext(), BibliaActivity.class);
                                i.putExtra("nm_livro", book);
                                i.putExtra("livroSelecionado", themes.getLivro());
                                i.putExtra("capitulo", themes.getCapitulo());
                                i.putExtra("versiculo", themes.getVerso());
                                startActivity(i);

                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                VersesBibleModel themes = listVerses.get(position);

                                DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());
                                String book = dataBaseAcess.findBook(themes.getLivro());

                                ClipboardManager clipboardManager = (ClipboardManager) getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("Versiculos", themes.getText() + "\n\n" + book + " " + themes.getCapitulo() + ":" + themes.getVerso());
                                assert clipboardManager != null;
                                clipboardManager.setPrimaryClip(clipData);

                                Alerter.create(SearchVersesAllActivity.this)
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


    private void listVersesofTheme(String text) {


        listVerses.clear();

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

        List<VersesBibleModel> lista2;

        lista2 = dataBaseAcess.listAllVerses(text);

        listVerses.addAll(lista2);

        adapter.notifyDataSetChanged();
    }
}
