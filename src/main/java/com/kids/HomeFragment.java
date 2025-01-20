package com.kids;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private ImageButton amharic, english, maths, game;
    private GridView gridView;
    private VideoItemAdapter adapter;
    private List<VideoItem> allVideoItems;
    private List<VideoItem> filteredVideoItems;
    private DatabaseReference databaseRef;

    private VideoView currentVideoView = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);

        // Initialize Firebase reference
        databaseRef = FirebaseDatabase.getInstance().getReference("videos");

        // Initialize UI components
        amharic = view.findViewById(R.id.amharic);
        english = view.findViewById(R.id.english);
        maths = view.findViewById(R.id.maths);
        game = view.findViewById(R.id.game);
        gridView = view.findViewById(R.id.grid_view);

        // Initialize the lists
        allVideoItems = new ArrayList<>();
        filteredVideoItems = new ArrayList<>();
        adapter = new VideoItemAdapter(getContext(), filteredVideoItems);
        gridView.setAdapter(adapter);

        // Fetch video data from Firebase
        fetchVideosFromFirebase();

        // Set button listeners for filtering
        amharic.setOnClickListener(v -> filterVideos("Amharic"));
        english.setOnClickListener(v -> filterVideos("English"));
        maths.setOnClickListener(v -> filterVideos("Maths"));
        game.setOnClickListener(v -> filterVideos("Game"));

        return view;
    }

    private void fetchVideosFromFirebase() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allVideoItems.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String title = snapshot.child("title").getValue(String.class);
                    Boolean isApproved = snapshot.child("isApproved").getValue(Boolean.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String videoUrl = snapshot.child("url").getValue(String.class);

                    if (title != null && description != null && videoUrl != null && Boolean.TRUE.equals(isApproved)) {
                        VideoItem videoItem = new VideoItem(title, isApproved, description, videoUrl);
                        allVideoItems.add(videoItem);
                    }
                }

                filteredVideoItems.clear();
                filteredVideoItems.addAll(allVideoItems);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("HomeFragment", "Error fetching video data: " + databaseError.getMessage());
            }
        });
    }

    private void filterVideos(String title) {
        // Pause the currently playing video if any
        if (currentVideoView != null && currentVideoView.isPlaying()) {
            currentVideoView.pause();
        }

        filteredVideoItems.clear();
        for (VideoItem item : allVideoItems) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                filteredVideoItems.add(item);
            }
        }

        adapter.notifyDataSetChanged();
        if (filteredVideoItems.isEmpty()) {
            Toast.makeText(getContext(), "No videos available for " + title, Toast.LENGTH_SHORT).show();
        }
    }
}
