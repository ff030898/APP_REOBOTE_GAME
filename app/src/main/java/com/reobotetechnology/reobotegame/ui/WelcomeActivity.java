package com.reobotetechnology.reobotegame.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.dao.DataBaseAcess;
import com.reobotetechnology.reobotegame.model.PerguntasModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends AppCompatActivity {


    Button btnCadastro, btnGoogle;
    TextView txtLogin;
    ImageView logo;
    ConstraintLayout itens;
    ProgressBar progressoIniciar;
    private static final int version = 2;

    //Animation
    Animation topAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        btnGoogle = findViewById(R.id.btnGoogle);
        btnCadastro = findViewById(R.id.btnEmail);
        itens = findViewById(R.id.itens);
        progressoIniciar = findViewById(R.id.progressoIniciar);
        txtLogin = findViewById(R.id.txtLogin);
        destacarTextoComNegrito(txtLogin.getText().toString(), "Login", txtLogin);
        logo = findViewById(R.id.logo);

        btnCadastro.setVisibility(View.GONE);
        btnGoogle.setVisibility(View.GONE);
        itens.setVisibility(View.GONE);
        txtLogin.setVisibility(View.GONE);
        logo.setVisibility(View.GONE);

        //progressoIniciar = findViewById(R.id.progressoIniciar);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                iniciar();


            }
        }, 3000);

        try {
            inicializarBancoDeDados();
        }catch(Exception e){
            alert("Erro; "+e.getMessage());
        }

    }

    public static void destacarTextoComNegrito(String textoCompleto, String textoDestaque,
                                               final TextView txtDesc) {

        int start = textoCompleto.indexOf(textoDestaque);
        int end = start + textoDestaque.length();

        Spannable spannableText = new SpannableString(textoCompleto);
        spannableText.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);

        txtDesc.setText(spannableText);

    }

    public void iniciar(){

        progressoIniciar.setVisibility(View.GONE);
        btnCadastro.setVisibility(View.VISIBLE);
        btnGoogle.setVisibility(View.VISIBLE);
        txtLogin.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);
        logo.setAnimation(topAnim);
        itens.setVisibility(View.VISIBLE);
        itens.setAnimation(bottomAnim);
    }

    public void login(View view){
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    public void cadastro(View view){
        startActivity(new Intent(getApplicationContext(), CadastroActivity.class));
    }

    public void home(View view){
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    private void inicializarBancoDeDados() {

        DataBaseAcess dataBaseAcess = DataBaseAcess.getInstance(getApplicationContext());

        try {

            if(version > 1) {
                dataBaseAcess.onUpdate();
            }else{
                dataBaseAcess.onCreate();
            }

        }catch (Exception e){
            alert("ERRO: "+e.getMessage());
        }

    }

    private void alert(String s) {
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }



}
