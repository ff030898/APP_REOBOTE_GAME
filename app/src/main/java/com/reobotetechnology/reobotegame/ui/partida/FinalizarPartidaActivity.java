package com.reobotetechnology.reobotegame.ui.partida;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.reobotetechnology.reobotegame.R;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FinalizarPartidaActivity extends AppCompatActivity {

    String resultado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar_partida);


        Bundle extras = getIntent().getExtras();
        assert extras != null;
        resultado = extras.getString("resultado");

        TextView textView = findViewById(R.id.textView);
        textView.setText(resultado);
    }
}
