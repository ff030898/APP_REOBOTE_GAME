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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.utils.ChecarSegundoPlano;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ConfirmationMatchActivity extends AppCompatActivity {

    private int cont;
    private Timer time;
    private boolean exit = false;

    private ProgressBar progressBar;
    private LinearLayout linearConfirmation;
    private TextView textTime, textDescription;

    private String name;

    //Animation
    private Animation topAnim;
    private ImageButton btn_back;

    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_match);

        progressBar = findViewById(R.id.progressBar);
        linearConfirmation = findViewById(R.id.linearConfirmation);
        textTime = findViewById(R.id.textTime);
        textDescription = findViewById(R.id.textDescription);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        name = extras.getString("name");

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        String title = getString(R.string.partidasM);
        String description = "Você vs "+name;

        txt_title.setText(title);
        txt_subtitle.setText(description);

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

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
        ConfirmationMatchActivity.Task task = new ConfirmationMatchActivity.Task();
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
                    String description = name+" aceitou o seu convite";
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
