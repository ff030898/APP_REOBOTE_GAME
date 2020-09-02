package com.reobotetechnology.reobotegame.ui.partida;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.reobotetechnology.reobotegame.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;


import de.hdodenhof.circleimageview.CircleImageView;

public class FinalizarPartidaActivity extends AppCompatActivity {

    String resultado;
    private LinearLayout linearB, linearTopo;
    private CircleImageView imagemPerfil, imagemPerfil2;
    private TextView txtNome1, txtNome2, txtPontos1, txtPontos2;
    private FloatingActionButton btnSair;

    //Animation
    Animation topAnim, bottomAnim;

    private FirebaseAuth autenticacao = ConfiguracaoFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_partida);

        imagemPerfil = findViewById(R.id.imagemPerfil);
        imagemPerfil2 = findViewById(R.id.imagemPerfil2);
        linearTopo = findViewById(R.id.linearTopo);
        linearB = findViewById(R.id.linearB);
        txtNome1 = findViewById(R.id.txtNome1);
        txtNome2 = findViewById(R.id.txtNome2);
        txtPontos1 = findViewById(R.id.txtPontos1);
        txtPontos2 = findViewById(R.id.txtPontos2);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        linearB.setAnimation(bottomAnim);
        //linearTopo.setAnimation(topAnim);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            this.carregaUser();
        }

        Bundle extras = getIntent().getExtras();
        assert extras != null;
        resultado = extras.getString("resultado");
        String imagem = extras.getString("imagem");
        String nomeJogador2 = extras.getString("jogador2");

        txtPontos1.setText("" + extras.getInt("pontos"));

        try {

            assert nomeJogador2 != null;
            if (nomeJogador2.equals("Reobote")) {

                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(imagemPerfil2);
            } else {
                if(imagem != null){
                    Glide
                            .with(getApplicationContext())
                            .load(imagem)
                            .centerCrop()
                            .placeholder(R.drawable.user)
                            .into(imagemPerfil2);
                }else {
                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.reobote)
                            .centerCrop()
                            .placeholder(R.drawable.user)
                            .into(imagemPerfil2);
                }
            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.reobote)
                    .centerCrop()
                    .placeholder(R.drawable.user)
                    .into(imagemPerfil2);
        }

        txtNome2.setText(nomeJogador2);
        txtPontos2.setText("" + extras.getInt("pontos2"));


        TextView textView = findViewById(R.id.textView);
        String textoTopo = "";

        if (resultado.equals("vitoria")) {
            textoTopo = "EBA, VOCÊ VENCEU!";
        } else if (resultado.equals("empate")) {
            textoTopo = "DEU EMPATE!";
        } else {
            textoTopo = "OH NÃO, VOCÊ PERDEU!";
        }
        textView.setText(textoTopo);


        btnSair = findViewById(R.id.btnSair);

        btnSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void carregaUser() {

        String[] linhas = user.getDisplayName().split(" ");
        txtNome1.setText(linhas[0]);

        try {

            if (user.getPhotoUrl() == null) {
                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.user)
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(imagemPerfil);
            } else {

                Glide
                        .with(getApplicationContext())
                        .load(user.getPhotoUrl())
                        .centerCrop()
                        .placeholder(R.drawable.user)
                        .into(imagemPerfil);
            }

        } catch (Exception e) {
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.user)
                    .centerCrop()
                    .placeholder(R.drawable.user)
                    .into(imagemPerfil);
        }

    }

}
