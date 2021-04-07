package com.reobotetechnology.reobotegame.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.ui.main.WelcomeActivity;
import com.reobotetechnology.reobotegame.ui.match.MatchRulesActivity;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
    private FirebaseUser user = autenticacao.getCurrentUser();
    private DatabaseReference firebaseRef = ConfigurationFireBase.getFirebaseDataBase();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnLogoff = findViewById(R.id.btnLogoff);

        btnLogoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoff();
            }
        });

        Button btnRules = findViewById(R.id.btnRules);

        btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewRules();
            }
        });

    }


    private void logoff(){

        autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();
        autenticacao.signOut();

        startActivity(new Intent(getApplicationContext(), WelcomeActivity.class));
        finish();
    }

    private void viewRules(){
        startActivity(new Intent(getApplicationContext(), MatchRulesActivity.class));
    }
}
