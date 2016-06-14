package com.osh.hackathonbrowser.model;

import com.osh.hackathonbrowser.Constants;

public class FakePrize {
    private String title;

    //@SerializedName("image_url")
    private String imageUrl;

    private String description;

    public FakePrize(String title, String imageUrl, String description) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public static FakePrize getFakePrize(){
        return new FakePrize("Some Headphones", Constants.DEFAULT_FAKE_PRIZE, "Generously provided by Headphone Maker, Inc.");
    }
}
