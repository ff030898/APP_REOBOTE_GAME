package com.reobotetechnology.reobotegame.ui.friends;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.reobotetechnology.reobotegame.R;


public class ViewImageScreenActivity extends AppCompatActivity {

    TextView txtNomeImagem;
    ImageView imgVoltarImagem, imagemUsuario;
    String imagem;
    String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }


        txtNomeImagem = findViewById(R.id.txtNomeImg);
        imgVoltarImagem = findViewById(R.id.imgVoltarImagem);
        imagemUsuario = findViewById(R.id.imagemUsuario);

        Bundle extras = getIntent().getExtras();

        assert extras != null;
        imagem = extras.getString("imagem");
        nome = extras.getString("nome");

        txtNomeImagem.setText(nome);
        try{
            if(nome.equals("Reobote IA")){

                Glide
                        .with(getApplicationContext())
                        .load(R.drawable.reobote)
                        .centerCrop()
                        .placeholder(R.drawable.profile)
                        .into(imagemUsuario);
            }else {
                if (imagem.isEmpty()) {

                    Glide
                            .with(getApplicationContext())
                            .load(R.drawable.profile)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(imagemUsuario);
                } else {

                    Glide
                            .with(getApplicationContext())
                            .load(imagem)
                            .centerCrop()
                            .placeholder(R.drawable.profile)
                            .into(imagemUsuario);
                }
            }
        }catch (Exception e){
            Glide
                    .with(getApplicationContext())
                    .load(R.drawable.profile)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(imagemUsuario);
        }


        imgVoltarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
