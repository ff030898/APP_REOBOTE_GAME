package com.reobotetechnology.reobotegame.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.reobotetechnology.reobotegame.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class PerfilActivity extends AppCompatActivity {

    CircleImageView imagemPerfil;
    String nome = "Fabrício Ferreira";
    Button btn_editar_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        getSupportActionBar().setTitle("Perfil");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagemPerfil = findViewById(R.id.imgPerfil);
        btn_editar_perfil = findViewById(R.id.btn_editar_perfil);

        btn_editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), EditarPerfilActivity.class));
            }
        });

        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), ImagemActivity.class);
                i.putExtra("nome", nome);
                i.putExtra("imagem", R.drawable.eu);
                startActivity( i );
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ver_todos, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.menu_ver_todos_amigos:
                startActivity(new Intent(getApplicationContext(), AmigosActivity.class));
                finish();
                break;

            case R.id.menu_seguindo:
                startActivity(new Intent(getApplicationContext(), AmigosActivity.class));
                finish();
                break;

            case R.id.menu_seguidores:
                startActivity(new Intent(getApplicationContext(), AmigosActivity.class));
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
