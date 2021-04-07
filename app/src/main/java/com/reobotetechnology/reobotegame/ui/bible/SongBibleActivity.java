package com.reobotetechnology.reobotegame.ui.bible;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.reobotetechnology.reobotegame.R;
import com.tapadoo.alerter.Alerter;

public class SongBibleActivity extends AppCompatActivity {

    private boolean song = false;

    //Animation
    private Animation topAnim;

    //private ProgressBar progressBar;
    private ImageButton btn_back;
    private ImageView btnPlay;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_bible);

        //Vem da Activity Principal
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String nm_book = extras.getString("nm_book");
            int cap_book = extras.getInt("cap_book");

            //TOOLBAR
            TextView txt_title = findViewById(R.id.txt_title);
            TextView txt_subtitle = findViewById(R.id.txt_subtitle);


            txt_title.setText(nm_book);
            txt_subtitle.setText("Capítulo "+cap_book);

            //book
            TextView txtSigla = findViewById(R.id.txtSigla);
            assert nm_book != null;
            txtSigla.setText(nm_book.substring(0,2));
        }

        btn_back = findViewById(R.id.btn_back);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);

        ImageButton btn_download = findViewById(R.id.btn_settings);

        btn_download.setVisibility(View.VISIBLE);
        btn_download.setImageResource(R.drawable.ic_download);

        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Alerter.create(SongBibleActivity.this)
                        .setTitle("Oops...")
                        .setText("Download indisponível no momento!")
                        .setIcon(R.drawable.ic_alert)
                        .setDuration(2000)
                        .setBackgroundColorRes(R.color.colorWarning)
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Alerter.hide();
                            }
                        })
                        .show();
            }
        });

        //progressBar.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //progressBar.setVisibility(View.GONE);
                btn_back.setAnimation(topAnim);

            }
        }, 2000);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!song){
                    btnPlay.setImageResource(R.drawable.ic_pause);
                    Alerter.create(SongBibleActivity.this)
                            .setTitle("Oops...")
                            .setText("A bíblia por aúdio ainda não está disponível!")
                            .setIcon(R.drawable.ic_alert)
                            .setDuration(2000)
                            .setBackgroundColorRes(R.color.colorWarning)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Alerter.hide();
                                }
                            })
                            .show();
                    song = true;
                }else{
                    btnPlay.setImageResource(R.drawable.ic_play);
                    song = false;
                }
            }
        });
    }
}
