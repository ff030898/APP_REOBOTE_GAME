package com.reobotetechnology.reobotegame.ui.partida;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reobotetechnology.reobotegame.R;

import java.util.Objects;

public class ModoPartidaActivity extends AppCompatActivity {

    ProgressBar progressoIniciar;
    Button btnOnline, btnOffline;
    LinearLayout linearRegras;
    TextView txtLerMais;
    //Animation
    Animation topAnim, bottomAnim;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modo_partida);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Escolha um Modo");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressoIniciar = findViewById(R.id.progressoIniciar);
        linearRegras = findViewById(R.id.linearRegras);
        btnOnline = findViewById(R.id.btnOnline);
        btnOffline = findViewById(R.id.btnOffline);
        txtLerMais = findViewById(R.id.txtLerMais);

        progressoIniciar.setVisibility(View.VISIBLE);
        linearRegras.setVisibility(View.GONE);
        btnOffline.setVisibility(View.GONE);
        btnOnline.setVisibility(View.GONE);


        txtLerMais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegrasActivity.class));
            }
        });

        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PartidaActivity.class);
                i.putExtra("nome", "Reobote");
                i.putExtra("imagem", "");
                startActivity( i );
                finish();

            }
        });

        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AmigosOnlineActivity.class));
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressoIniciar.setVisibility(View.GONE);
                linearRegras.setVisibility(View.VISIBLE);
                btnOffline.setVisibility(View.VISIBLE);
                btnOnline.setVisibility(View.VISIBLE);


            }
        }, 2000);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sair, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish(); // Finaliza a Activity atual e assim volta para a tela anterior
                break;
            case R.id.menu_sair:
                finish();
                break;

            default:
                break;
        }
        return true;
    }

}
