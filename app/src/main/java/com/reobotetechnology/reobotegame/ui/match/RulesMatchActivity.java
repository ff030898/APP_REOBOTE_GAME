package com.reobotetechnology.reobotegame.ui.match;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reobotetechnology.reobotegame.R;

public class RulesMatchActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_rules_match);



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
                startActivity(new Intent(getApplicationContext(), ConfirmationMatchActivity.class));
            }
        });

        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MatchActivity.class);
                i.putExtra("nome", "Reobote");
                i.putExtra("imagem", "");
                startActivity( i );
                finish();

            }
        });

        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FriendsOnlineActivity.class));
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


}
