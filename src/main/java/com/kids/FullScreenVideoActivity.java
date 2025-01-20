package com.kids;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class FullScreenVideoActivity extends AppCompatActivity {

    private VideoView fullscreenVideoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen_video);

        fullscreenVideoView = findViewById(R.id.f);

        Uri videoUri = getIntent().getData();
        fullscreenVideoView.setVideoURI(videoUri);

        fullscreenVideoView.setOnPreparedListener(mp -> fullscreenVideoView.start());

        fullscreenVideoView.setOnCompletionListener(mp -> finish());
    }
}
