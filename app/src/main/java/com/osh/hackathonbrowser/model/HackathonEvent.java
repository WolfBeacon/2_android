package com.osh.hackathonbrowser.model;

public class HackathonEvent {
    private String title;
    private String subtitle;
    private String description;
    private String iconUrl;

    public HackathonEvent(String title, String subtitle, String description, String iconUrl) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }
}
