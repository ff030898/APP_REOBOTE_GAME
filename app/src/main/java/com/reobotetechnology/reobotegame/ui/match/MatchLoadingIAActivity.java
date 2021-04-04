package com.reobotetechnology.reobotegame.ui.match;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.utils.ChecarSegundoPlano;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class MatchLoadingIAActivity extends AppCompatActivity {

    private int cont;
    private Timer time;
    private boolean exit = false;

    private ProgressBar progressBar;
    private LinearLayout linearConfirmation;
    private TextView textTime, textDescription;
    private Button btnDesistir;

    private String name;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back;

    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();

    CircleImageView imagemPerfil, imagemPerfil2;
    TextView txtUsuario1, txtUsuario2;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_ia_match);

        progressBar = findViewById(R.id.progressBar);
        linearConfirmation = findViewById(R.id.linearConfirmation);
        textTime = findViewById(R.id.textTime);
        textDescription = findViewById(R.id.txtDescription);
        imagemPerfil = findViewById(R.id.imagemPerfil);
        imagemPerfil2 = findViewById(R.id.imagemPerfil2);
        btnDesistir = findViewById(R.id.btnDesistir);
        txtUsuario1 = findViewById(R.id.txtUsuario1);
        txtUsuario2 = findViewById(R.id.txtUsuario2);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        name = extras.getString("name");

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        String title = ("Você vs "+getString(R.string.name_robot));
        String description = "Aguardando confirmação";

        txt_title.setText(title);
        txt_subtitle.setText(description);
        String [] name = user.getDisplayName().split(" ");
        txtUsuario1.setText(name[0]);
        txtUsuario2.setText(getString(R.string.name_robot));

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        loadUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                btn_back.setAnimation(topAnim);
                Timer();

            }
        }, 3000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnDesistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void loadUser(){
        try {

            if (user.getPhotoUrl() == null) {

                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imagemPerfil);
            } else {

                Glide
                        .with(getApplicationContext())
                        .load(user.getPhotoUrl())
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imagemPerfil);
            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imagemPerfil);


        }

        Glide
                .with(getApplicationContext())
                .load(R.drawable.reobote)
                .centerCrop()
                .placeholder(R.drawable.profile)
                .into(imagemPerfil2);
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void Timer() {

        cont = 10;
        textTime.setText("10");

        if (time != null) {
            time.cancel();
            time = null;
        }
        time = new Timer();
        MatchLoadingIAActivity.Task task = new MatchLoadingIAActivity.Task();
        time.schedule(task, 1000, 1000);

    }

    class Task extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @SuppressLint("SetTextI18n")
                @Override
                public void run() {

                    progressBar.setVisibility(View.GONE);
                    linearConfirmation.setVisibility(View.VISIBLE);
                    String description = "O seu convite foi aceito";
                    TextView txt_subtitle = findViewById(R.id.txt_subtitle);
                    txt_subtitle.setText(description);

                        if (cont > 0) {

                            cont = cont - 1;
                            textTime.setText("0" + cont);
                            textDescription.setText(description.toUpperCase());
                            if (cont <= 3) {
                                textDescription.setText("ATENÇÃO! A PARTIDA JÁ VAI COMEÇAR...");
                            }

                        } else {

                            try {

                                time.cancel();
                                boolean foregroud = new ChecarSegundoPlano().execute(getApplicationContext()).get();
                                if (foregroud && !exit) {
                                      initalMatch(name);
                                }

                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }


                }


            });
        }

    }

    private void abortMatch() {

        try {
            exit = true;
            finish();
        }catch(Exception ignored){

        }

    }

    private void initalMatch(String name){

        if(name.equals(getString(R.string.name_robot))) {
            Intent i = new Intent(getApplicationContext(), MatchActivity.class);
            i.putExtra("nome", name);
            i.putExtra("imagem", "");
            startActivity(i);
            exit = true;
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        if(cont > 0) {
            abortMatch();
        }else {
            exit = true;
            super.onBackPressed();
        }

    }
}
