package com.reobotetechnology.reobotegame.ui.bible;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.reobotetechnology.reobotegame.adapter.ThemesVersesOfBibleAdapters;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.ThemesModel;

import java.util.ArrayList;
import java.util.List;

public class BibleThemesListActivity extends AppCompatActivity {

    private RecyclerView recyclerThemesList;
    private ThemesVersesOfBibleAdapters adapter;
    private List<ThemesModel> listThemes = new ArrayList<>();

    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back;


    private CoordinatorLayout constraintPrincipal;

    //Params
    String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_themes_list);

        progressBar = findViewById(R.id.progressBar3);
        constraintPrincipal = findViewById(R.id.constraintPrincipal);
        constraintPrincipal.setVisibility(View.GONE);

        recyclerThemesList = findViewById(R.id.recyclerThemesList);

        adapter = new ThemesVersesOfBibleAdapters(listThemes, getApplicationContext());

        //RecyclerThemes
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerThemesList.setLayoutManager(layoutManager);
        recyclerThemesList.setHasFixedSize(true);

        recyclerThemesList.setAdapter(adapter);

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.themes));
        txt_subtitle.setText(getString(R.string.themes_description));

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
        recyclerThemesList.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerThemesList,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                ThemesModel themes = listThemes.get(position);

                                Intent i = new Intent(getApplicationContext(), ThemesActivity.class);
                                i.putExtra("theme", themes.getThemeText());
                                startActivity(i);

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

    private void listThemesVerses() {

        listThemes.clear();

        listThemes.add(new ThemesModel("Amor"));
        listThemes.add(new ThemesModel("Amizade"));
        listThemes.add(new ThemesModel("Ansiedade"));
        listThemes.add(new ThemesModel("Namoro"));
        listThemes.add(new ThemesModel("Casamento"));

        adapter.notifyDataSetChanged();


    }

    @Override
    protected void onStart() {
        super.onStart();
        listThemesVerses();
    }
}
