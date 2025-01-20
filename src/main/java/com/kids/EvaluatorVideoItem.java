package com.kids;

public class EvaluatorVideoItem {
    private String id;
    private String title;
    private boolean isApproved;
    private String description;
    private String videoUrl;

    public EvaluatorVideoItem(String id, String title, boolean isApproved, String description, String videoUrl) {
        this.id = id;
        this.title = title;
        this.isApproved = isApproved;
        this.description = description;
        this.videoUrl = videoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
