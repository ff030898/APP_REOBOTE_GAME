package com.reobotetechnology.reobotegame.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import com.reobotetechnology.reobotegame.R;

public class PlayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        VideoView videoview = findViewById(R.id.videoView);

        //Esconder a StstusBar e barra de navegação

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Executar Video
        videoview.setMediaController( new MediaController(this));
        videoview.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.video);
        videoview.start();
    }
}
