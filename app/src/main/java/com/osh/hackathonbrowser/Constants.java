package com.osh.hackathonbrowser;

public class Constants {
    public static final String DEFAULT_FAKE_IMAGE = "http://static1.squarespace.com/static/53d7e9a8e4b0113709a865f9/t/55c046a6e4b00500e1403ce2/1438664366675/concepting.jpg?format=original";
    public static final String DEFAULT_FAKE_PRIZE = "http://www.harderbloggerfaster.com/wp-content/uploads/2012/02/Headphone.png";
    public static final String DEFAULT_FAKE_SPEAKER = "https://financialpostbusiness.files.wordpress.com/2014/11/john-chen2.jpg?w=620&quality=60&strip=all&h=464";
    public static final String DEFAULT_FAKE_COMPANY = "https://img.stackshare.io/service/43/rrsj746a.png";
    public static final String DEFAULT_FAKE_MAP = "http://www.columbia.edu/~bo8/img/columbiaMap.png";
    public static final String DEFAULT_FAKE_URL = "http://todo.com";

    //"NYC"
    public static final Double DEFAULT_LONGITUDE = 40.73;
    public static final Double DEFAULT_LATITUDE = -73.95;

    /** A listing of IDs that wouldn't fit in XML. **/
    public class Ids {
        public static final int TIME_UPDATE_NOTIFICATION_ID = 500;
        public static final int TIME_UPDATE_REQUEST_CODE = 1000;
    }

    public class URLs {
        public static final String API_URL = "http://api.wolfbeacon.com/v1/";
    }

    public class Prefs {
        public static final String CREDS_STRING = "credentials";
        public static final String HACKATHON_STARTS_END_BOOL = "hackathon_start_ends_pref";
        public static final String EVENT_REMINDER_BOOL = "event_reminders_pref";
    }
}