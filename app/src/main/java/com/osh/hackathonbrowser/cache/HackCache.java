package com.osh.hackathonbrowser.cache;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.osh.hackathonbrowser.ApplicationClass;
import com.osh.hackathonbrowser.api.response.HackathonResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A cache of hackathons you might care about. Used to cache top 10 closest hackathons and hackathons
 * that are favorited or that you're attending.
 */
public class HackCache {
    private static final String CACHE_FILE = "hackathon_cache";

    private static HackCache mInstance;

    private static final String PREF_CACHED_FLOATING_IDS_SET= "cached_soft_ids";
    private static final String PREF_CACHED_PINNED_IDS_SET = "cached_hard_ids";
    private static final String PREF_CACHED_ENTRY_FLOATING_PREFIX_STRING = "cached_hackathon_f_";
    private static final String PREF_CACHED_ENTRY_PINNED_PREFIX_STRING = "cached_hackathon_p_";

    private SharedPreferences cachePrefs;
    private Gson gson;

    private Set<String> hackathonIdFloatingSet;
    private Set<String> hackathonIdPinnedSet;
    private Map<String, CacheEntry> hackathonFloatingMap;
    private Map<String, CacheEntry> hackathonPinnedMap;

    private HackCache(){
        Context appContext = ApplicationClass.getAppContext();
        cachePrefs = appContext.getSharedPreferences(CACHE_FILE, 0);
        gson = new Gson();
        hackathonFloatingMap = new HashMap<>(50);
        hackathonPinnedMap = new HashMap<>(50);
    }

    public static HackCache getInstance(){
        return mInstance == null ? (mInstance = new HackCache()) : mInstance;
    }

    /**
     * Prepares the cache for use. Should be done before we start any queries (e.g. ApplicationClass).
     */
    public synchronized void warm(){
        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {
                //(1) Load list of hackathon IDs
                hackathonIdFloatingSet = cachePrefs.getStringSet(PREF_CACHED_FLOATING_IDS_SET, new HashSet<String>());
                hackathonIdPinnedSet = cachePrefs.getStringSet(PREF_CACHED_PINNED_IDS_SET, new HashSet<String>());

                //(2) Load associated data into memory
                loadProcedure(hackathonIdFloatingSet, PREF_CACHED_FLOATING_IDS_SET,
                        PREF_CACHED_ENTRY_FLOATING_PREFIX_STRING, hackathonFloatingMap);
                loadProcedure(hackathonIdPinnedSet, PREF_CACHED_PINNED_IDS_SET,
                        PREF_CACHED_ENTRY_PINNED_PREFIX_STRING, hackathonPinnedMap);
                return null;
            }
        }.execute();
    }

    private void loadProcedure(Set<String> idSet, String idPref, String prefix, Map<String, CacheEntry> map){
        SharedPreferences.Editor editor = cachePrefs.edit();

        //(2) For each hackathon ID, get the associated HackathonResponse text and deserialize
        Set<String> toRemove = new HashSet<>();
        for(String id : idSet){
            String serializedHack = cachePrefs.getString(prefix + id, null);
            if(serializedHack == null){
                toRemove.add(id);
                continue;
            }

            CacheEntry entry = gson.fromJson(serializedHack, CacheEntry.class);
            if(entry.getEntryCacheVersion() < CacheEntry.CACHE_ENTRY_VERSION){
                editor.remove(prefix + id);
                toRemove.add(id);
                continue;
            }

            map.put(id, entry);
        }
        idSet.removeAll(toRemove);
        editor.putStringSet(idPref, idSet);
        editor.commit();
    }

    /**
     * Update the floating entries in the cache.
     * @param responses A list (ideally a smaller size) of hackathons to keep around in case
     *                  we lose network.
     */
    public synchronized void updateFloatingEntries(List<HackathonResponse> responses){
        SharedPreferences.Editor editor = cachePrefs.edit();

        hackathonFloatingMap.clear();
        for(String s : hackathonIdFloatingSet){
            editor.remove(PREF_CACHED_ENTRY_FLOATING_PREFIX_STRING + s);
        }
        hackathonIdFloatingSet.clear();

        for(HackathonResponse response : responses){
            CacheEntry entry = new CacheEntry(response);
            hackathonIdFloatingSet.add(String.valueOf(response.getId()));
            hackathonFloatingMap.put(String.valueOf(response.getId()), entry);
            editor.putString(PREF_CACHED_ENTRY_FLOATING_PREFIX_STRING + response.getId(), gson.toJson(entry));
        }

        editor.putStringSet(PREF_CACHED_FLOATING_IDS_SET, hackathonIdFloatingSet);
        editor.commit();
    }

    /**
     * Pin a hackathon to the cache. If it exists, it will be updated.
     * @param response The Hackathon to pin.
     * @param becauseAttending Are we pinning because we're attending this?
     * @param becauseFavorited Are we pinning becauase we favorited this?
     */
    public synchronized void pinHackathon(HackathonResponse response, boolean becauseAttending, boolean becauseFavorited){
        SharedPreferences.Editor editor = cachePrefs.edit();
        CacheEntry existingEntry = hackathonPinnedMap.get(String.valueOf(response.getId()));

        //Make sure we persist all the old reasons
        if(existingEntry != null){
            int[] reasons = existingEntry.getPinReasons();
            for(int reason : reasons){
                if(reason == CacheEntry.PINNED_BECAUSE_ATTENDING) becauseAttending = true;
                if(reason == CacheEntry.PINNED_BECAUSE_FAVORITE) becauseFavorited = true;
            }
        }

        hackathonIdPinnedSet.add(String.valueOf(response.getId()));
        hackathonPinnedMap.put(String.valueOf(response.getId()), new CacheEntry(response, becauseFavorited, becauseAttending));

        editor.putString(PREF_CACHED_ENTRY_PINNED_PREFIX_STRING + response.getId(),
                gson.toJson(new CacheEntry(response, becauseFavorited, becauseAttending)));
        editor.putStringSet(PREF_CACHED_FLOATING_IDS_SET, hackathonIdPinnedSet);
        editor.commit();
    }

    /**
     * Unpin a hackathon from the cache.
     * @param hackathonId The hackathon to unpin.
     * @param becauseNotAttending Are we unpinning because we're no longer attending?
     * @param becauseNotFavorited Are we unpinning because it's not favorited any more?
     */
    public synchronized void unpinHackathon(long hackathonId, boolean becauseNotAttending, boolean becauseNotFavorited){
        SharedPreferences.Editor editor = cachePrefs.edit();

        //So... this is trickier than floating hackathons. We need to check why we're unpinning, because
        //we could attend an unfavorited hackathon, in which case we should keep the data in the cache and
        //just update the reasons why we have it in the cache.
        CacheEntry existingEntry = hackathonPinnedMap.get(String.valueOf(hackathonId));
        boolean keepBasedOnAttending = false;
        boolean keepBasedOnFavorited = false;
        if(existingEntry != null){
            int[] reasons = existingEntry.getPinReasons();
            for(int reason : reasons){
                if(reason == CacheEntry.PINNED_BECAUSE_ATTENDING) keepBasedOnAttending = true;
                if(reason == CacheEntry.PINNED_BECAUSE_FAVORITE) keepBasedOnFavorited = true;
            }
        }

        //We were keeping it for two reasons; both now invalid
        if(keepBasedOnAttending && keepBasedOnFavorited && becauseNotAttending && becauseNotFavorited){
            keepBasedOnAttending = false;
            keepBasedOnFavorited = false;
        } else if (keepBasedOnAttending && becauseNotAttending && !keepBasedOnFavorited){
            //We were keeping it because we were attending ALONE, and now we're not attending
            keepBasedOnAttending = false;
        } else if (keepBasedOnFavorited && becauseNotFavorited && !keepBasedOnAttending){
            //We were keeping it because we were favorited ALONE, and now we're not favorited
            keepBasedOnFavorited = false;
        } else {
            //We were keeping it because we favorited/are attending, and we're unpinning for another reason, so we should keep!
        }

        if(keepBasedOnAttending || keepBasedOnFavorited){ //Keep it around!
            //Just update existing entry -- this is a map change, not an ID set change.
            existingEntry.setPinReasons(keepBasedOnFavorited, keepBasedOnAttending);
            editor.putString(PREF_CACHED_ENTRY_PINNED_PREFIX_STRING + hackathonId, gson.toJson(existingEntry));
        } else { //Trash it.
            hackathonIdPinnedSet.remove(String.valueOf(hackathonId));
            hackathonPinnedMap.remove(String.valueOf(hackathonId));

            editor.remove(PREF_CACHED_ENTRY_PINNED_PREFIX_STRING + hackathonId);
            editor.putStringSet(PREF_CACHED_FLOATING_IDS_SET, hackathonIdPinnedSet);
        }

        editor.commit();
    }

    public boolean isHackathonFavorited(long hackathonId){
        CacheEntry entry = hackathonPinnedMap.get(String.valueOf(hackathonId));
        return entry != null && entry.pinnedBecauseFavorite();
    }

    public List<HackathonResponse> getFavoriteHackathons(){
        List<HackathonResponse> favoriteHacks = new ArrayList<>((int) (hackathonPinnedMap.size() * 0.7));
        for(Map.Entry<String, CacheEntry> pinnedHacks : hackathonPinnedMap.entrySet()){
            if(pinnedHacks.getValue().pinnedBecauseFavorite()) favoriteHacks.add(pinnedHacks.getValue().getHackathon());
        }
        return favoriteHacks;
    }

    public HackathonResponse getPinnedHackathon(long hackathonId){
        return hackathonPinnedMap.get(String.valueOf(hackathonId)).getHackathon();
    }
}
