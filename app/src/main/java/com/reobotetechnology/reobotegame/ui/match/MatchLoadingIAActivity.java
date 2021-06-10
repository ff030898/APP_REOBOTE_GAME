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
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.MatchModel;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;
import com.reobotetechnology.reobotegame.utils.ChecarSegundoPlano;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

public class MatchLoadingIAActivity extends AppCompatActivity {

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

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
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
        Button btnDesistir = findViewById(R.id.btnDesistir);
        txtUsuario1 = findViewById(R.id.txtUsuario1);
        txtUsuario2 = findViewById(R.id.txtUsuario2);

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        name = extras.getString("name");

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        String title = ("Você vs " + getString(R.string.name_robot));

        txt_title.setText(title);
        txt_subtitle.setText(getString(R.string.descriptionMatch));
        String[] name = Objects.requireNonNull(user.getDisplayName()).split(" ");
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

    private void loadUser() {
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
            new SweetAlertDialog(MatchLoadingIAActivity.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Desistir da Partida")
                    .setContentText("Tem certeza que deseja desistir ?")
                    .setConfirmText("Sim")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            exit = true;
                            sDialog.hide();
                            finish();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initalMatch(String name) {

        try {

            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");

            Calendar cal = Calendar.getInstance();
            Date data = new Date();
            cal.setTime(data);
            Date data_atual = cal.getTime();

            String idPartida = dateFormat.format(data_atual);

            MatchModel partida = new MatchModel(idPartida, false, false, false, true, "", idPartida, user.getEmail(), "0", "0", "");
            partida.salvar();

            String email_robot = getString(R.string.email_robot);

            //Começa com 0 pontos cada jogador
            String jogador1 = Base64Custom.codificarBase64(Objects.requireNonNull(Objects.requireNonNull(autenticacao.getCurrentUser()).getEmail()));
            DatabaseReference usuarioRef = firebaseRef.child("partidas").child(idPartida);
            usuarioRef.child(jogador1).setValue(0);

            //Começa com 0 pontos cada jogador
            String jogador2 = Base64Custom.codificarBase64(Objects.requireNonNull(Objects.requireNonNull(email_robot)));
            DatabaseReference usuarioRef2 = firebaseRef.child("partidas").child(idPartida);
            usuarioRef2.child(jogador2).setValue(0);

        if (name.equals(getString(R.string.name_robot))) {
            Intent i = new Intent(getApplicationContext(), MatchActivity.class);
            i.putExtra("id", idPartida);
            i.putExtra("nome", name);
            i.putExtra("imagem", "");
            i.putExtra("email", email_robot);
            startActivity(i);
            finish();
        }

        } catch (Exception ignored) {

        }

    }

    @Override
    public void onBackPressed() {
        if (cont > 0) {
            abortMatch();
        } else {
            exit = true;
            super.onBackPressed();
        }

    }
}
