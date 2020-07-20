package com.reobotetechnology.reobotegame.ui.visualizar_imagens;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reobotetechnology.reobotegame.R;
import com.squareup.picasso.Picasso;

public class VisualizarImagemActivity extends AppCompatActivity {

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
            if(nome.equals("Reobote Technology")){
                Picasso.get().load(R.drawable.reobote).into(imagemUsuario);
            }else {
                if (imagem.isEmpty()) {
                    Picasso.get().load(R.drawable.user).into(imagemUsuario);
                } else {
                    Picasso.get().load(imagem).into(imagemUsuario);
                }
            }
        }catch (Exception e){
            Picasso.get().load(R.drawable.user).into(imagemUsuario);
        }


        imgVoltarImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


}
