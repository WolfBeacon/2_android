package com.osh.hackathonbrowser.model;

import com.osh.hackathonbrowser.Constants;

public class FakeGuestSpeaker {
    private String name;

    //@SerializedName("image_url")
    private String imageUrl;

    private String description;

    public FakeGuestSpeaker(String name, String imageUrl, String description) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public static FakeGuestSpeaker getFakeGuestSpeaker(){
        return new FakeGuestSpeaker("Guest Speaker", Constants.DEFAULT_FAKE_SPEAKER, "A lecture on blah, blah, blah");
    }
}
