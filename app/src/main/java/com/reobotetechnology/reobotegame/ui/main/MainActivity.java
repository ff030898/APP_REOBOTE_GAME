package com.reobotetechnology.reobotegame.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.reobotetechnology.reobotegame.R;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;
import com.reobotetechnology.reobotegame.ui.home.HomeActivity;


public class MainActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        setButtonBackVisible(false);
        setButtonNextVisible(false);

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .fragment(R.layout.slide_1)
                .backgroundDark(android.R.color.white)
                .build()
        );

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .backgroundDark(android.R.color.white)
                .fragment(R.layout.slide_2)
                .build()
        );

        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .backgroundDark(android.R.color.white)
                .fragment(R.layout.slide_3)
                .build()
        );


        addSlide( new FragmentSlide.Builder()
                .background(android.R.color.white)
                .backgroundDark(android.R.color.white)
                .fragment(R.layout.slide_4)
                //Ultimo Slide
                .canGoForward(false)
                .build()
        );



    }

    protected void onStart(){
        super.onStart();
        usuarioLogado();
    }

    public void usuarioLogado(){

        FirebaseAuth autenticacao = ConfigurationFireBase.getFirebaseAutenticacao();

        if(autenticacao.getCurrentUser() != null){
            startActivity(new Intent(this, HomeActivity.class));
        }
    }

    public void signIn(View view){
        startActivity(new Intent(this, WelcomeActivity.class));
        finish();
    }



}

