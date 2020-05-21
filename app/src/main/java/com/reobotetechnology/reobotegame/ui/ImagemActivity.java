package com.reobotetechnology.reobotegame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.reobotetechnology.reobotegame.R;

public class ImagemActivity extends AppCompatActivity {

    TextView txtNomeImagem;
    ImageView imgVoltarImagem, imagemUsuario;
    int imagem;
    String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagem);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.BLACK);
        }

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        txtNomeImagem = findViewById(R.id.txtNomeImg);
        imgVoltarImagem = findViewById(R.id.imgVoltarImagem);
        imagemUsuario = findViewById(R.id.imagemUsuario);

        Bundle extras = getIntent().getExtras();

        imagem = extras.getInt("imagem");
        nome = extras.getString("nome");

        txtNomeImagem.setText(nome);
        imagemUsuario.setImageResource(imagem);

        imgVoltarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
