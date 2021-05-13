package com.reobotetechnology.reobotegame.ui.main;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.adapter.BibleAdapters;
import com.reobotetechnology.reobotegame.adapter.SettingsAdapters;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.helper.RecyclerItemClickListener;
import com.reobotetechnology.reobotegame.model.SettingsModel;
import com.reobotetechnology.reobotegame.model.VersesBibleModel;
import com.reobotetechnology.reobotegame.ui.admin.blog.CreatePostBlogActivity;
import com.reobotetechnology.reobotegame.ui.bible.BiblieActivity;
import com.reobotetechnology.reobotegame.ui.bible.SearchVersesAllActivity;
import com.reobotetechnology.reobotegame.ui.main.WelcomeActivity;
import com.reobotetechnology.reobotegame.ui.match.MatchLoadingIAActivity;
import com.reobotetechnology.reobotegame.ui.match.MatchRulesActivity;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();

    private RecyclerView recyclerSettings;
    private SettingsAdapters adapter;
    private List<SettingsModel> listSettings = new ArrayList<>();

    private ProgressBar progressBar;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back;


    private CoordinatorLayout coordinatorMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressBar = findViewById(R.id.progressBar3);
        coordinatorMain = findViewById(R.id.coordinatorMain);
        coordinatorMain.setVisibility(View.GONE);

        recyclerSettings = findViewById(R.id.recyclerSettings);

        //configurarAdapter
        adapter = new SettingsAdapters(listSettings, getApplicationContext());

        //RecyclerThemes
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerSettings.setLayoutManager(layoutManager);
        recyclerSettings.setAdapter(adapter);


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.configura_es));
        txt_subtitle.setText(getString(R.string.leia_com_aten_o));

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                coordinatorMain.setVisibility(View.VISIBLE);
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
        recyclerSettings.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerSettings,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                SettingsModel settingsModel = listSettings.get(position);

                                int item = settingsModel.getId();

                                if (item == 1) {
                                    editProfile();
                                } else if (item == 2) {
                                    viewPolitices();
                                } else if (item == 3) {
                                    viewRules();
                                } else if (item == 4) {
                                    viewHelp();
                                } else if (item == 5) {
                                    rulesOfThermes();
                                } else if (item == 6) {
                                    insertPost();
                                } else if (item == 7) {
                                    logoff();
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

    private void listSettings() {

        listSettings.clear();

        listSettings.add(new SettingsModel(1, getString(R.string.edit_perfil)));
        listSettings.add(new SettingsModel(2, getString(R.string.politica_de_privacidadeM)));
        listSettings.add(new SettingsModel(3, getString(R.string.regras_do_jogo)));
        listSettings.add(new SettingsModel(4, getString(R.string.ajuda)));
        listSettings.add(new SettingsModel(5, getString(R.string.regras_termosM)));
        String email = user.getEmail();
        assert email != null;
        if (email.equals("fabricio@gmail.com") || email.equals("fabricioferreiradossantos1998@gmail.com")) {
            listSettings.add(new SettingsModel(6, "Adicionar post"));
        }
        listSettings.add(new SettingsModel(7, getString(R.string.sair2)));
        adapter.notifyDataSetChanged();
    }


    private void editProfile() {
        startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));

    }


    private void logoff() {

        try {
            new SweetAlertDialog(SettingsActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Fazer Logoff")
                    .setContentText("Tem certeza que deseja deslogar da sua conta ?")
                    .setConfirmText("Sim")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
                            autenticacao.signOut();

                            startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
                            finish();
                            sDialog.hide();
                        }
                    }).setCancelText("Não")
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            sweetAlertDialog.hide();
                        }
                    })
                    .show();

        } catch (Exception ignored) {

        }

    }

    private void viewPolitices() {
        startActivity(new Intent(getApplicationContext(), PoliticesActivity.class));
    }

    private void viewRules() {
        startActivity(new Intent(getApplicationContext(), MatchRulesActivity.class));
    }

    private void viewHelp() {
        startActivity(new Intent(getApplicationContext(), HelpActivity.class));
    }

    private void rulesOfThermes() {
        startActivity(new Intent(getApplicationContext(), TermesofUseActivity.class));
    }


    private void insertPost() {
        startActivity(new Intent(getApplicationContext(), CreatePostBlogActivity.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        listSettings();
    }
}
