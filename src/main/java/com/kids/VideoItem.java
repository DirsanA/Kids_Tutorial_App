package com.kids;

public class VideoItem {
    private String title;
    private boolean isApproved;
    private String description;
    private String videoUrl;  // Change to store video URL

    public VideoItem(String title, boolean isApproved, String description, String videoUrl) {
        this.title = title;
        this.isApproved = isApproved;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    public String getTitle() {
        return title;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }
}
