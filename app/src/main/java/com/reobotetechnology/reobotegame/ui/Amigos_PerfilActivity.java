package com.reobotetechnology.reobotegame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.reobotetechnology.reobotegame.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class Amigos_PerfilActivity extends AppCompatActivity {

    Button btnSeguir;
    CircleImageView imagemPerfil;
    TextView txtRanking, txtNomePerfil;
    int id, imagem, ranking;
    String nome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos_perfil);


        btnSeguir = findViewById(R.id.btnSeguir);
        imagemPerfil = findViewById(R.id.imagemPerfil);
        txtRanking = findViewById(R.id.txtRanking);
        txtNomePerfil = findViewById(R.id.nomePerfil);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        imagem = extras.getInt("imagem");
        ranking = extras.getInt("ranking");
        nome = extras.getString("nome");


        txtNomePerfil.setText(nome);
        imagemPerfil.setImageResource(imagem);
        txtRanking.setText(ranking+"º");

        getSupportActionBar().setTitle(nome);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imagemPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), ImagemActivity.class);
                i.putExtra("nome", nome);
                i.putExtra("imagem", imagem);
                startActivity( i );
            }
        });


        btnSeguir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String texto = btnSeguir.getText().toString();

                if(texto.equals("SEGUIR")) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        int background = R.drawable.btn_screen2;
                        btnSeguir.setBackground(getResources().getDrawable(background));
                    }

                    btnSeguir.setText("SEGUINDO");
                    btnSeguir.setTextColor(ColorStateList.valueOf(0xff000000));

                }else{
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                        int background = R.drawable.btn_screen;
                        btnSeguir.setBackground(getResources().getDrawable(background));

                    }

                    btnSeguir.setText("SEGUIR");
                    btnSeguir.setTextColor(ColorStateList.valueOf(0xffffffff));
                }
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
