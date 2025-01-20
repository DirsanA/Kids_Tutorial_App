package com.kids;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class VideoItemAdapter extends ArrayAdapter<VideoItem> {
    private Context context;
    private Handler handler = new Handler();
    private VideoView currentVideoView = null;

    public VideoItemAdapter(@NonNull Context context, List<VideoItem> videoItems) {
        super(context, 0, videoItems);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.video_view_layout, parent, false);
        }

        VideoItem currentItem = getItem(position);

        VideoView videoView = itemView.findViewById(R.id.video);
        TextView textViewTitle = itemView.findViewById(R.id.title);
        TextView textViewDescription = itemView.findViewById(R.id.description);
        SeekBar videoSeekBar = itemView.findViewById(R.id.videoSeekBar);
        Button fullscreenButton = itemView.findViewById(R.id.fullscreenButton);
        ImageView playPauseButton = itemView.findViewById(R.id.playPauseButton);

        textViewTitle.setText(currentItem.getTitle());
        textViewDescription.setText(currentItem.getDescription());

        Uri videoUri = Uri.parse(currentItem.getVideoUrl());
        videoView.setVideoURI(videoUri);

        // Reset to the beginning when switching categories or videos
        videoView.seekTo(0);
        videoView.pause(); // Pause the video and reset it to the start

        videoView.setOnPreparedListener(mp -> {
            videoSeekBar.setMax(mp.getDuration());
            videoSeekBar.setProgress(0);
        });

        // Handle completion of the video
        videoView.setOnCompletionListener(mp -> {
            playPauseButton.setImageResource(R.drawable.play_icon);
            currentVideoView = null;  // Reset the current video view
        });

        videoView.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                playPauseButton.setImageResource(R.drawable.play_icon);
            } else {
                // Pause previous video if it's playing
                if (currentVideoView != null && currentVideoView != videoView) {
                    currentVideoView.pause();
                }
                videoView.start();
                currentVideoView = videoView;
                playPauseButton.setImageResource(R.drawable.pause_icon);
                updateSeekBar(videoView, videoSeekBar);
            }
        });

        playPauseButton.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                playPauseButton.setImageResource(R.drawable.play_icon);
            } else {
                // Pause previous video if it's playing
                if (currentVideoView != null && currentVideoView != videoView) {
                    currentVideoView.pause();
                }
                videoView.start();
                currentVideoView = videoView;
                playPauseButton.setImageResource(R.drawable.pause_icon);
                updateSeekBar(videoView, videoSeekBar);
            }
        });

        videoSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    videoView.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        fullscreenButton.setOnClickListener(v -> {
            // Implement fullscreen functionality if needed
        });

        return itemView;
    }

    private void updateSeekBar(VideoView videoView, SeekBar videoSeekBar) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    videoSeekBar.setProgress(videoView.getCurrentPosition());
                    handler.postDelayed(this, 500);
                }
            }
        }, 500);
    }
}
