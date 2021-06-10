package com.reobotetechnology.reobotegame.ui.match;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.reobotetechnology.reobotegame.R;

import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.helper.Base64Custom;
import com.reobotetechnology.reobotegame.model.UserModel;
import com.tapadoo.alerter.Alerter;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.reobotetechnology.reobotegame.R.drawable.btn_quiz_timer_blue;
import static com.reobotetechnology.reobotegame.R.drawable.btn_quiz_timer_green;
import static com.reobotetechnology.reobotegame.R.drawable.btn_quiz_timer_red;

public class MatchFinishDetailsActivity extends AppCompatActivity {

    private String resultado, imageUserInvite, nameUserInvite, nameUser, imageUser, emailJogador;
    private int scoreUser, scoreUserInvite, view;
    private CircleImageView imagemPerfil, imagemPerfil2, imageProfileWinner, imageProfileEquals1, imageProfileEquals2;
    private TextView txtNome1, txtNome2, txtPontos1, txtPontos2, txtResultDescription, txtDateTime, txtScoreTop,
            txtScoreCorrect, txtScoreIncorrect, txtScoreMedia, txtScoreMediaTitle, textGrapich;

    //toolbar
    private TextView txt_title, txt_subtitle;
    Animation modal_anima;


    //Animation
    private Animation topAnim;
    private ImageButton btn_back;

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();
    private FirebaseUser user = autenticacao.getCurrentUser();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_match_details);

        //Animação da Modal de Tempo Esgotado
        modal_anima = AnimationUtils.loadAnimation(this, R.anim.modal_animation);


        //TOOLBAR
        txt_title = findViewById(R.id.txt_title);
        txt_subtitle = findViewById(R.id.txt_subtitle);

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        imagemPerfil = findViewById(R.id.imagemPerfil);
        imagemPerfil2 = findViewById(R.id.imagemPerfil2);
        imageProfileWinner = findViewById(R.id.imageProfileWinner);
        imageProfileEquals1 = findViewById(R.id.imageProfileEquals1);
        imageProfileEquals2 = findViewById(R.id.imageProfileEquals2);
        txtDateTime = findViewById(R.id.textView48);
        Button btn_close = findViewById(R.id.btn_close2);
        Button btn_share = findViewById(R.id.btn_share);

        txtNome1 = findViewById(R.id.txtNome1);
        txtNome2 = findViewById(R.id.txtNome2);
        txtPontos1 = findViewById(R.id.txtPontos1);
        txtPontos2 = findViewById(R.id.txtPontos2);
        txtResultDescription = findViewById(R.id.txtResultDescription);
        txtScoreTop = findViewById(R.id.txtScoreTop);

        textGrapich = findViewById(R.id.textGrapich);
        txtScoreCorrect = findViewById(R.id.txtScoreCorrect);
        txtScoreIncorrect = findViewById(R.id.txtScoreIncorrect);
        txtScoreMedia = findViewById(R.id.txtScoreMedia);
        txtScoreMediaTitle = findViewById(R.id.txtScoreMediaTitle);


        Bundle extras = getIntent().getExtras();
        assert extras != null;
        resultado = extras.getString("resultado");
        imageUser = extras.getString("imagem");
        imageUserInvite = extras.getString("imagem2");
        nameUser = extras.getString("jogador");
        emailJogador = extras.getString("emailJogador");
        nameUserInvite = extras.getString("jogador2");
        scoreUser = extras.getInt("pontos");
        scoreUserInvite = extras.getInt("pontos2");
        view = extras.getInt("view");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.loadResult();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                btn_back.setAnimation(topAnim);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(view == 1){
                    finish();
                }else {
                    availabled();
                }
            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if(view == 1){
                    finish();
                }else {
                    availabled();
                }
            }
        });

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alerter.create(MatchFinishDetailsActivity.this)
                        .setTitle("Oops...")
                        .setText("Opção indisponível no momento!")
                        .setIcon(R.drawable.ic_alert)
                        .setDuration(2000)
                        .setBackgroundColorRes(R.color.colorWarning)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Alerter.hide();
                            }
                        })
                        .show();
            }
        });


    }

    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void loadResult() {

        txtNome1.setText(nameUser);
        txtPontos1.setText("" + scoreUser);

        try {

            if (imageUser != null) {
                Glide
                        .with(getApplicationContext())
                        .load(imageUser)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imagemPerfil);
            }

            else {
                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile).into(imagemPerfil);

            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imagemPerfil);
        }


        try {

            if (nameUserInvite.equals(getString(R.string.name_robot))) {

                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imagemPerfil2);
            } else {
                if (imageUserInvite != null) {
                    Glide
                            .with(getApplicationContext())
                            .load(imageUserInvite)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(imagemPerfil2);
                } else {
                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.reobote)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(imagemPerfil2);
                }
            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.reobote)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imagemPerfil2);
        }

        txtNome2.setText(nameUserInvite);
        txtPontos2.setText("" + scoreUserInvite);


        String textoResult;

        if(emailJogador.equals(user.getEmail())) {

            if (resultado.equals("vitoria")) {
                textoResult = "Você ganhou :) ";
                txtResultDescription.setText(textoResult.toUpperCase());
                winnerUser();

            } else if (resultado.equals("empate")) {
                textoResult = "Você empatou :| ";
                txtResultDescription.setText(textoResult.toUpperCase());
                txtResultDescription.setTextColor(ColorStateList.valueOf(0xffffd700));
                winnerScoreEquals();
            } else {
                textoResult = "Você perdeu :( ";
                txtResultDescription.setText(textoResult.toUpperCase());
                txtResultDescription.setTextColor(ColorStateList.valueOf(0xff801b20));
                winnerUserInvite();
            }


            txt_title.setText(getString(R.string.detailsMatch));
            txt_subtitle.setText(textoResult);

        }else{
            if (resultado.equals("vitoria")) {
                textoResult = nameUser+" ganhou :) ";
                txtResultDescription.setText(textoResult.toUpperCase());
                winnerUser();

            } else if (resultado.equals("empate")) {
                textoResult = nameUser+" empatou :| ";
                txtResultDescription.setText(textoResult.toUpperCase());
                txtResultDescription.setTextColor(ColorStateList.valueOf(0xffffd700));
                winnerScoreEquals();
            } else {
                textoResult = nameUser+" perdeu :( ";
                txtResultDescription.setText(textoResult.toUpperCase());
                txtResultDescription.setTextColor(ColorStateList.valueOf(0xff801b20));
                winnerUserInvite();
            }


            txt_title.setText(getString(R.string.partidarUser)+" "+nameUser);
            txt_subtitle.setText(textoResult);
        }

        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatNotification = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        Calendar cal = Calendar.getInstance();
        Date data = new Date();
        cal.setTime(data);
        Date data_atual = cal.getTime();

        String dateNotification = dateFormatNotification.format(data);
        String time = timeFormat.format(data_atual);

        txtDateTime.setText(dateNotification + " - " + time);
        txtScoreTop.setText(scoreUser + " x " + scoreUserInvite);

        int countQuestions = 10;

        txtScoreCorrect.setText("" + scoreUser);
        int erros = (countQuestions - scoreUser);
        txtScoreIncorrect.setText("" + erros);
        int media = ((scoreUser * 100) / countQuestions);
        txtScoreMedia.setText(media + "%");
        textGrapich.setText(scoreUser + "/" + countQuestions);

        if (media <= 40) {
            textGrapich.setBackground(getResources().getDrawable(btn_quiz_timer_red));
            textGrapich.setTextColor(ColorStateList.valueOf(0xff801b20));
            txtScoreMedia.setTextColor(ColorStateList.valueOf(0xff801b20));
            txtScoreMediaTitle.setTextColor(ColorStateList.valueOf(0xff801b20));
        } else if (media >= 50 && media < 70) {
            textGrapich.setBackground(getResources().getDrawable(btn_quiz_timer_blue));
            textGrapich.setTextColor(ColorStateList.valueOf(0xff0066cc));
            txtScoreMedia.setTextColor(ColorStateList.valueOf(0xff0066cc));
            txtScoreMediaTitle.setTextColor(ColorStateList.valueOf(0xff0066cc));
        } else if (media >= 70) {
            textGrapich.setBackground(getResources().getDrawable(btn_quiz_timer_green));
            textGrapich.setTextColor(ColorStateList.valueOf(0xff247b37));
            txtScoreMedia.setTextColor(ColorStateList.valueOf(0xff247b37));
            txtScoreMediaTitle.setTextColor(ColorStateList.valueOf(0xff247b37));
        }

    }

    private void winnerScoreEquals() {
        imageProfileWinner.setVisibility(View.GONE);
        imageProfileEquals1.setVisibility(View.VISIBLE);
        imageProfileEquals2.setVisibility(View.VISIBLE);
        try {

            if (user.getPhotoUrl() == null) {
                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imageProfileEquals1);
            } else {

                Glide
                        .with(getApplicationContext())
                        .load(imageUser)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imageProfileEquals1);
            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imageProfileEquals1);
        }

        try {

            if (nameUserInvite.equals(getString(R.string.name_robot))) {

                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imageProfileEquals2);
            } else {
                if (imageUserInvite != null) {
                    Glide
                            .with(getApplicationContext())
                            .load(imageUserInvite)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(imageProfileEquals2);
                } else {
                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.reobote)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(imageProfileEquals2);
                }
            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imageProfileEquals2);
        }

    }

    private void winnerUser() {
        imageProfileWinner.setVisibility(View.VISIBLE);
        imageProfileEquals1.setVisibility(View.GONE);
        imageProfileEquals2.setVisibility(View.GONE);
        try {

            if (user.getPhotoUrl() == null) {
                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.profile)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imageProfileWinner);
            } else {

                Glide
                        .with(getApplicationContext())
                        .load(imageUser)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imageProfileWinner);
            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imageProfileWinner);
        }

    }

    private void winnerUserInvite() {
        imageProfileWinner.setVisibility(View.VISIBLE);

        try {

            if (nameUserInvite.equals(getString(R.string.name_robot))) {

                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imageProfileWinner);
            } else {
                if (imageUserInvite != null) {
                    Glide
                            .with(getApplicationContext())
                            .load(imageUserInvite)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(imageProfileWinner);
                } else {
                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.reobote)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(imageProfileWinner);
                }
            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imageProfileWinner);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void availabled() {
        try {

            final String idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
            firebaseRef.child("usuarios").child(idUsuario).addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    try {
                        UserModel user = dataSnapshot.getValue(UserModel.class);

                        assert user != null;
                        if (!user.isAvailabled()) {
                            openModal();
                        } else {
                            finish();
                        }
                    } catch (Exception e) {
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception ignored) {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private void openModal() {
        try {
            final Dialog welcomeModal = new Dialog(this);
            welcomeModal.requestWindowFeature(Window.FEATURE_NO_TITLE);
            welcomeModal.setCancelable(false);
            welcomeModal.setContentView(R.layout.include_modal_availabled);

            CardView cardModal = welcomeModal.findViewById(R.id.cardModal);
            cardModal.startAnimation(modal_anima);
            CircleImageView profile = welcomeModal.findViewById(R.id.profile);
            final RatingBar availabled = welcomeModal.findViewById(R.id.rating);
            final TextView txtStartSelected = welcomeModal.findViewById(R.id.txtStartSelected);
            Button btnAction = welcomeModal.findViewById(R.id.btnSend);

            txtStartSelected.setText("5/5");

            availabled.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    String valor = String.valueOf((int) (availabled.getRating()));

                    switch (valor) {
                        case "1":
                            txtStartSelected.setText(valor + "/5");
                            break;
                        case "2":

                            txtStartSelected.setText(valor + "/5");
                            break;
                        case "3":
                            txtStartSelected.setText(valor + "/5");
                            break;
                        case "4":
                            txtStartSelected.setText(valor + "/5");
                            break;
                        case "5":
                            txtStartSelected.setText(valor + "/5");
                            break;
                    }
                }
            });


            if (imageUser != null) {

                Glide
                        .with(getApplicationContext())
                        .load(imageUser)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(profile);
            }

            btnAction.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    welcomeModal.dismiss();
                    //sendAvailabled
                    String idUsuario = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        idUsuario = Base64Custom.codificarBase64(Objects.requireNonNull(user.getEmail()));
                    }
                    assert idUsuario != null;
                    DatabaseReference userRefDB = firebaseRef.child("usuarios").child(idUsuario);
                    userRefDB.child("availabled").setValue(true);
                    Toast.makeText(getApplicationContext(), "Obrigado por avaliar !", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Objects.requireNonNull(welcomeModal.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
            welcomeModal.setCancelable(false);

            welcomeModal.show();

        } catch (Exception ignored) {

        }

    }

}
