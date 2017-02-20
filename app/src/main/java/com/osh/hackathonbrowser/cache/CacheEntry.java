package com.osh.hackathonbrowser.cache;

import com.osh.hackathonbrowser.api.response.HackathonResponse;

public class CacheEntry {
    /**
     * Versioning for cache entries. Presumably we'll need to dump the cache when the API starts throwing us new stuff.
     */
    public static final int CACHE_ENTRY_VERSION = 1;

    public static final int PINNED_BECAUSE_FAVORITE = 1;
    public static final int PINNED_BECAUSE_ATTENDING = 2;

    private int version;
    private int[] pinnedBecause;
    private HackathonResponse hackathon;

    public CacheEntry(HackathonResponse hackathon) {
        this.hackathon = hackathon;
        this.version = CACHE_ENTRY_VERSION;
        this.pinnedBecause = new int[0];
    }

    public CacheEntry(HackathonResponse hackathon, boolean pinnedAsFavorite, boolean pinnedAsAttending) {
        this.hackathon = hackathon;
        this.version = CACHE_ENTRY_VERSION;
        setPinReasons(pinnedAsFavorite, pinnedAsAttending);
    }

    public HackathonResponse getHackathon() {
        return hackathon;
    }

    public int getEntryCacheVersion() {
        return version;
    }

    public int[] getPinReasons() {
        return pinnedBecause;
    }

    public void setPinReasons(boolean pinnedAsFavorite, boolean pinnedAsAttending) {
        int count = (pinnedAsAttending ? 1 : 0) + (pinnedAsFavorite ? 1 : 0);
        this.pinnedBecause = new int[count];
        if(count == 1){
            if(pinnedAsAttending)
                pinnedBecause[0] = PINNED_BECAUSE_ATTENDING;
            else
                pinnedBecause[0] = PINNED_BECAUSE_FAVORITE;
        } else if (count == 2) {
            pinnedBecause[0] = PINNED_BECAUSE_FAVORITE;
            pinnedBecause[1] = PINNED_BECAUSE_ATTENDING;
        } else {
            throw new RuntimeException("You must create a pinned cache entry for *some* reason...");
        }
    }

    public boolean pinnedBecauseFavorite(){
        for(int i : pinnedBecause){
            if(i == PINNED_BECAUSE_FAVORITE) return true;
        }
        return false;
    }

    public boolean pinnedBecauseAttending(){
        for(int i : pinnedBecause){
            if(i == PINNED_BECAUSE_ATTENDING) return true;
        }
        return false;
    }
}
