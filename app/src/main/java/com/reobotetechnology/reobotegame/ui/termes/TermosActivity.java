package com.reobotetechnology.reobotegame.ui.termes;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reobotetechnology.reobotegame.R;

public class TermosActivity extends AppCompatActivity {

    //Animation
    private Animation topAnim;

    private ProgressBar progressBar;
    private LinearLayout linearTermes;
    private ImageButton btn_back;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termos);

        progressBar = findViewById(R.id.progressBar);
        linearTermes = findViewById(R.id.linearTermes);


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.regras_termosM));
        txt_subtitle.setText(getString(R.string.leia_com_aten_o));

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        linearTermes.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                linearTermes.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
