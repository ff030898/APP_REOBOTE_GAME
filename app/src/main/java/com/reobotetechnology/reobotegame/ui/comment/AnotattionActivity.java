package com.reobotetechnology.reobotegame.ui.comment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reobotetechnology.reobotegame.R;
import com.tapadoo.alerter.Alerter;

public class AnotattionActivity extends AppCompatActivity {

    //Animation
    private Animation topAnim;

    private ProgressBar progressBar;
    private LinearLayout linearMain;
    private ImageButton btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anotattion);

        progressBar = findViewById(R.id.progressBar);
        linearMain = findViewById(R.id.linearMain);


        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText(getString(R.string.sobre_mim));
        txt_subtitle.setText(getString(R.string.leia_com_aten_o));

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        linearMain.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                linearMain.setVisibility(View.VISIBLE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button btn_screen = findViewById(R.id.btn_screen);

        btn_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAnottation();
            }
        });
    }

    private void saveAnottation() {

        Alerter.create(this)
                .setTitle("Obaa...")
                .setText("Status atualizado com sucesso!")
                .setIcon(R.drawable.ic_success)
                .setDuration(1500)
                .setBackgroundColorRes(R.color.colorGreen1)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Alerter.hide();
                    }
                })
                .show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();

            }
        }, 1900);

    }
}
