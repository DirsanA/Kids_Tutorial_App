package com.kids;

public class UploadVideo {
    private boolean isApproved;
    private String title; // Category name
    private String description;
    private String videoUrl;

    public UploadVideo() {
        // Default constructor required for Firebase
    }

    public UploadVideo(boolean isApproved,String title, String description, String videoUrl) {
       this.isApproved=isApproved;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    public boolean getIsApproved() {
        return isApproved;
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
