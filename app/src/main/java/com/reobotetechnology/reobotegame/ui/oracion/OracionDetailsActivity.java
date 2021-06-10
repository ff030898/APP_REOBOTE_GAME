package com.reobotetechnology.reobotegame.ui.oracion;

import androidx.appcompat.app.AppCompatActivity;

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

public class OracionDetailsActivity extends AppCompatActivity {

    //Animation
    private Animation topAnim;

    private ProgressBar progressBar;
    private LinearLayout linearLayout;
    private ImageButton btn_back;

    private String idOracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oracion_details);

        progressBar = findViewById(R.id.progressBar);
        linearLayout = findViewById(R.id.linearTermes);

        //Vem da Activity Principal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            idOracion = extras.getString("idOracion");
        }

        //TOOLBAR
        TextView txt_title = findViewById(R.id.txt_title);
        TextView txt_subtitle = findViewById(R.id.txt_subtitle);

        txt_title.setText("21 dias de Daniel");
        txt_subtitle.setText("Detalhes da campanha");

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
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
