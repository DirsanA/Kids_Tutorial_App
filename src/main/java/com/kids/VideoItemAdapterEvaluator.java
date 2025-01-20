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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class VideoItemAdapterEvaluator extends ArrayAdapter<EvaluatorVideoItem> {
    private Context context;
    private Handler handler = new Handler();
    private VideoView currentVideoView = null;

    public VideoItemAdapterEvaluator(@NonNull Context context, List<EvaluatorVideoItem> videoItems) {
        super(context, 0, videoItems);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.video_view_layout_evaluator, parent, false);
        }

        EvaluatorVideoItem currentItem = getItem(position);

        // Initialize views
        VideoView videoView = itemView.findViewById(R.id.video);
        TextView textViewTitle = itemView.findViewById(R.id.title);
        TextView textViewDescription = itemView.findViewById(R.id.description);
        SeekBar videoSeekBar = itemView.findViewById(R.id.videoSeekBar);
        Button fullscreenButton = itemView.findViewById(R.id.fullscreenButton);
        ImageView playPauseButton = itemView.findViewById(R.id.playPauseButton);
        Button accept = itemView.findViewById(R.id.accept);
        Button decline = itemView.findViewById(R.id.decline);

        // Set video details
        textViewTitle.setText(currentItem.getTitle());
        textViewDescription.setText(currentItem.getDescription());

        Uri videoUri = Uri.parse(currentItem.getVideoUrl());
        videoView.setVideoURI(videoUri);

        videoView.seekTo(0);
        videoView.pause(); // Pause video initially

        videoView.setOnPreparedListener(mp -> {
            videoSeekBar.setMax(mp.getDuration());
            videoSeekBar.setProgress(0);
        });

        videoView.setOnCompletionListener(mp -> {
            playPauseButton.setImageResource(R.drawable.play_icon);
            currentVideoView = null;
        });

        playPauseButton.setOnClickListener(v -> {
            if (videoView.isPlaying()) {
                videoView.pause();
                playPauseButton.setImageResource(R.drawable.play_icon);
            } else {
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

        // Approve video
        accept.setOnClickListener(v -> {
            DatabaseReference videoRef = FirebaseDatabase.getInstance().getReference("videos")
                    .child(currentItem.getId());
            videoRef.child("isApproved").setValue(true)
                    .addOnSuccessListener(aVoid -> {
                        remove(currentItem);
                        notifyDataSetChanged();
                    });
        });

        // Decline video
        decline.setOnClickListener(v -> {
            DatabaseReference videoRef = FirebaseDatabase.getInstance().getReference("videos")
                    .child(currentItem.getId());
            videoRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        remove(currentItem);
                        notifyDataSetChanged();
                    });
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
