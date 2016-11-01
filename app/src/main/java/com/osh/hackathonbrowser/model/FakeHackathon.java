package com.osh.hackathonbrowser.model;

import android.widget.TextView;

import com.google.gson.annotations.SerializedName;
import com.osh.hackathonbrowser.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class represents a *rough* idea of what the Hackathon model might eventually wind up
 * looking like. Modify and change name once the model has been finalized.
  */
public class FakeHackathon {
    private String title;

    @SerializedName("background_url")
    private String backgroundUrl;

    private String institution;

    private String description;

    @SerializedName("start_time_utc")
    private long startTimeUtc;

    @SerializedName("end_time_utc")
    private long endTimeUtc;

    @SerializedName("has_travel_reimbursement")
    private boolean hasTravelReimbursement;

    @SerializedName("bus_route_supported")
    private boolean hasBusRoute; //TODO: Generalize, perhaps?

    @SerializedName("travel_reimbursement")
    private String travelReimbursement;

    private String hardware;

    @SerializedName("is_starred")
    private boolean isStarred;

    private List<FakeSponsor> sponsors;

    private List<FakePrize> prizes;

    private List<FakeGuestSpeaker> speakers;

    private List<FakeMap> maps;

    private String twitter;

    @SerializedName("linkedin")
    private String linkedIn;

    private String facebook;

    private String website;

    public FakeHackathon(String title, String description, String institution, String backgroundUrl,
                         long startTimeUtc, long endTimeUtc, boolean hasTravelReimbursement,
                         boolean hasBusRoute, String travelReimbursement,
                         String hardware, boolean isStarred, List<FakeSponsor> sponsors,
                         List<FakePrize> prizes, List<FakeGuestSpeaker> speakers,
                         List<FakeMap> maps, String twitter, String linkedIn, String facebook,
                         String website) {
        this.title = title;
        this.description = description;
        this.institution = institution;
        this.backgroundUrl = backgroundUrl;
        this.startTimeUtc = startTimeUtc;
        this.endTimeUtc = endTimeUtc;
        this.hasTravelReimbursement = hasTravelReimbursement;
        this.hasBusRoute = hasBusRoute;
        this.travelReimbursement = travelReimbursement;
        this.hardware = hardware;
        this.isStarred = isStarred;
        this.sponsors = sponsors;
        this.prizes = prizes;
        this.speakers = speakers;
        this.maps = maps;
        this.twitter = twitter;
        this.linkedIn = linkedIn;
        this.facebook = facebook;
        this.website = website;
    }

    public String getTitle() {
        return title;
    }

    public String getInstitution() {
        return institution;
    }

    public String getDescription() {
        return description;
    }

    public long getStartTimeUtc() {
        return startTimeUtc;
    }

    public long getEndTimeUtc() {
        return endTimeUtc;
    }

    public boolean hasTravelReimbursement() {
        return hasTravelReimbursement;
    }

    public boolean hasBusRoute() {
        return hasBusRoute;
    }

    public String getTravelReimbursement() {
        return travelReimbursement;
    }

    public String getHardware() {
        return hardware;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public List<FakeSponsor> getSponsors() {
        return sponsors;
    }

    public List<FakePrize> getPrizes() {
        return prizes;
    }

    public List<FakeGuestSpeaker> getSpeakers() {
        return speakers;
    }

    public String getBackgroundUrl() {
        return backgroundUrl;
    }

    public List<FakeMap> getMaps() {
        return maps;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getWebsite() {
        return website;
    }

    public static FakeHackathon getFakeHackathon(){
        List<FakeSponsor> sponsors = new ArrayList<>();
        Collections.addAll(sponsors, FakeSponsor.getFakeSponsor(), FakeSponsor.getFakeSponsor(), FakeSponsor.getFakeSponsor());

        List<FakeGuestSpeaker> speakers = new ArrayList<>();
        Collections.addAll(speakers, FakeGuestSpeaker.getFakeGuestSpeaker(), FakeGuestSpeaker.getFakeGuestSpeaker(), FakeGuestSpeaker.getFakeGuestSpeaker(), FakeGuestSpeaker.getFakeGuestSpeaker());

        List<FakePrize> prizes = new ArrayList<>();
        Collections.addAll(prizes, FakePrize.getFakePrize(), FakePrize.getFakePrize(), FakePrize.getFakePrize(), FakePrize.getFakePrize(), FakePrize.getFakePrize(), FakePrize.getFakePrize());

        List<FakeMap> maps = new ArrayList<>();
        Collections.addAll(maps, FakeMap.getFakeMap(), FakeMap.getFakeMap());

        return new FakeHackathon(
                "A-Well-Known Hackathon",
                "A hackathon needed for blah, blah, and blah; extremely blah and very blah. Blah. Blah.",
                "Monto Vista University",
                Constants.DEFAULT_FAKE_IMAGE,
                System.currentTimeMillis() - (24 * 60 * 60 * 1000),
                System.currentTimeMillis() + (4 * 60 * 60 * 100),
                true,
                false,
                "We will reimburse your travel costs, up to $100, with proper receipts. Claims must be submitted by a week before the event.",
                "Oculus Rift, Arduinos, Raspberry Pis, Google Glass, and more!",
                false,
                sponsors,
                prizes,
                speakers,
                maps,
                "https://twitter.com/hackathon",
                "https://linkedin.com/hackathon",
                "https://facebook.com/hackathon",
                Constants.DEFAULT_FAKE_URL);
    }
}
