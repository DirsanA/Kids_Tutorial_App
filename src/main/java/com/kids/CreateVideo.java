package com.kids;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class CreateVideo extends Fragment {

    EditText video_description;
    Button upload_btn;
    ImageView static_image; // ImageView for displaying static image
    ProgressBar progressBar; // Progress bar to show upload progress
    private RadioGroup videoCategoryRadioGroup;
    DatabaseReference databaseRef;
    StorageReference storageRef;

    private static final int PICK_VIDEO_REQUEST = 1;
    Uri videoUri;

    public CreateVideo() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase references
        databaseRef = FirebaseDatabase.getInstance().getReference("videos");
        storageRef = FirebaseStorage.getInstance().getReference("videos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.create_video, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        video_description = view.findViewById(R.id.video_description);
        static_image = view.findViewById(R.id.placeholder_image); // Initialize ImageView
        upload_btn = view.findViewById(R.id.upload_btn);
        progressBar = view.findViewById(R.id.progress_bar); // Initialize progress bar
        videoCategoryRadioGroup = view.findViewById(R.id.video_title_radio_group);
        // Set click listener for static_image to open file chooser
        static_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        // Set click listener for upload button
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadVideo();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO_REQUEST);
    }

    private void uploadVideo() {
        // Ensure a video file is selected
        if (videoUri != null) {
            // Check if title and description are not empty
            int selectedId = videoCategoryRadioGroup.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = getView().findViewById(selectedId);
            String title = selectedRadioButton != null ? selectedRadioButton.getText().toString() : "";

            String description = video_description.getText().toString().trim();

            if (title.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter a title", Toast.LENGTH_SHORT).show();
                return;
            }

            if (description.isEmpty()) {
                Toast.makeText(getActivity(), "Please enter a description", Toast.LENGTH_SHORT).show();
                return;
            }

            // Show progress bar
            progressBar.setVisibility(View.VISIBLE);

            // Define path and filename in Firebase Storage
            String storagePath =  System.currentTimeMillis() + ".mp4";
            StorageReference fileRef = storageRef.child(storagePath);

            // Upload video to Firebase Storage
            UploadTask uploadTask = fileRef.putFile(videoUri);

            // Register observers to listen for when the upload is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Hide progress bar on failure
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "Failed to upload video: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Get download URL from Firebase Storage
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUri) {
                            // Upload video metadata to Firebase Realtime Database
                            String videoId = databaseRef.push().getKey();

                            if (videoId != null) {
                                Map<String, Object> videoMap = new HashMap<>();
                                videoMap.put("title", title);
                                videoMap.put("isApproved", false);
                                videoMap.put("description", description);
                                videoMap.put("url", downloadUri.toString());

                                databaseRef.child(videoId).setValue(videoMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // Clear fields
                                                videoCategoryRadioGroup.clearCheck();
                                                video_description.setText("");

                                                // Show static image after upload
                                                static_image.setImageResource(R.drawable.uploa);

                                                // Hide progress bar on success
                                                progressBar.setVisibility(View.GONE);

                                                // Inform user about success
                                                Toast.makeText(getActivity(), "Video uploaded successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })

                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Handle failures
                                                Toast.makeText(getActivity(), "Failed to upload video metadata: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                // Hide progress bar on failure
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        });
                            }
                        }
                    });
                }
            }).addOnProgressListener(taskSnapshot -> {
                // Calculate progress percentage
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                // Update progress bar
                progressBar.setProgress((int) progress);
            });
        } else {
            Toast.makeText(getActivity(), "Please select a video file", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            videoUri = data.getData();

            // Update static_image with a placeholder image (optional)
            static_image.setImageResource(R.drawable.uploa);
        }
    }
}