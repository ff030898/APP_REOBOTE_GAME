package com.reobotetechnology.reobotegame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.reobotetechnology.reobotegame.R;

public class LoginActivity extends AppCompatActivity {

    ProgressBar progresso;
    LinearLayout layout;
    FrameLayout frameLayout;
    Button btnEntrar, btnGoogle;
    View div_esq, div_dir;
    TextView txt_ou;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txt_ou = findViewById(R.id.txt_ou);
        btnGoogle = findViewById(R.id.btnGoogle);
        div_esq = findViewById(R.id.div_esq);
        div_dir = findViewById(R.id.div_dir);
        btnEntrar = findViewById(R.id.btnEntrar);
        frameLayout = findViewById(R.id.frameLayout);
        layout = findViewById(R.id.layout);
        progresso = findViewById(R.id.progresso);
        progresso.setVisibility(View.VISIBLE);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progresso.setVisibility(View.GONE);
                frameLayout.setVisibility(View.VISIBLE);
                layout.setVisibility(View.VISIBLE);
                txt_ou.setVisibility(View.VISIBLE);
                btnGoogle.setVisibility(View.VISIBLE);
                div_esq.setVisibility(View.VISIBLE);
                div_dir.setVisibility(View.VISIBLE);

            }
        }, 3000);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });


    }

    public void esqueceuSenha(View view) {

        startActivity(new Intent(getApplicationContext(), EsqueceuSenhaActivity.class));
        Toast.makeText(getApplicationContext(), "AINDA NÃO ESTÁ PRONTO", Toast.LENGTH_SHORT).show();

    }

    public void google(View view) {

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
