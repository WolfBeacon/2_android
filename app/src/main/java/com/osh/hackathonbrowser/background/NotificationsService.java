package com.osh.hackathonbrowser.background;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationsService extends Service {
	public static final String TAG = "NotificationsService";

	public static final String ON_HACKATHON_STARTED_ACTION = "com.osh.hackathonbrowser.ON_HACKATHON_STARTED";
	public static final String HACKATHON_ID_STRING_EXTRA = "hackathon_id"; //Extra type liable to change

	public static final String NOTIFY_REMAINING_TIME_ACTION = "com.osh.hackathonbrowser.NOTIFY_TIME_REMAINING";

	private static final int ONE_HOUR_MILLIS = 1000 * 60 * 60;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null || intent.getAction() == null) return START_NOT_STICKY;
		switch(intent.getAction()){
			case ON_HACKATHON_STARTED_ACTION:
				//Setup "there are x hours remaining" notifications every hour
				//TODO: Fetch a locally cached copy of the event from a provided EXTRA to get the end of the event
				long endTime = System.currentTimeMillis() + (1000 * 60 * 60 * 5); //5 hours
				if(endTime > System.currentTimeMillis() + ONE_HOUR_MILLIS){ //Some sort of timezone conversion will likely be needed here

				}
				return START_NOT_STICKY;
			case NOTIFY_REMAINING_TIME_ACTION:
				return START_NOT_STICKY;
			default:
				return START_NOT_STICKY;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
