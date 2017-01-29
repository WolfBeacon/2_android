package com.osh.hackathonbrowser.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

public class CalendarEntry {
    private long entryId;
    private Calendar startTime;
    private long eventDuration;
    private String eventName;
    private String eventLocation;
    private boolean goingTo;
    private boolean isGoingTo;

    public CalendarEntry(long entryId, Calendar startTime, long eventDuration, String eventName, String eventLocation, boolean goingTo) {
        this.entryId = entryId;
        this.startTime = startTime;
        this.eventDuration = eventDuration;
        this.eventName = eventName;
        this.eventLocation = eventLocation;
        this.goingTo = goingTo;
    }

    public long getEntryId() {
        return entryId;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public long getEventDuration() {
        return eventDuration;
    }

    public String getEventName() {
        return eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public boolean isGoingTo() {
        return goingTo;
    }

    /** Conveienence methods for getting dummy data. **/

    public static CalendarEntry getDummyEntry(int month, int dayOfMonth, int hour, int minute, String eventName, String location){
        Calendar dummy = new GregorianCalendar();
        dummy.set(Calendar.MONTH, month);
        dummy.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dummy.set(Calendar.HOUR_OF_DAY, hour);
        dummy.set(Calendar.MINUTE, minute);
        dummy.set(Calendar.SECOND, 0);
        dummy.set(Calendar.MILLISECOND, 0);

        return new CalendarEntry(new Random().nextLong(), dummy, new Random().nextInt(180) * 60 * 1000, eventName, location, false);
    }

    public static List<CalendarEntry> getDummyData() {
        List<CalendarEntry> toReturn = new ArrayList<>();

        toReturn.add(CalendarEntry.getDummyEntry(10, 12, 13, 30, "Brunch", "A nice buffet"));
        toReturn.add(CalendarEntry.getDummyEntry(10, 14, 14, 30, "Apps on the Apple ][", "N. Practical"));
        toReturn.add(CalendarEntry.getDummyEntry(10, 15, 14, 30, "Programming with Palm OS 3.5", "H. Spring"));
        toReturn.add(CalendarEntry.getDummyEntry(10, 16, 16, 30, "What's the fuss with v-blank?", "S. Ofamerica"));
        toReturn.add(CalendarEntry.getDummyEntry(10, 16, 17, 30, "Benefits of Mode 7", "M. Bros."));
        toReturn.add(CalendarEntry.getDummyEntry(10, 16, 14, 30, "Apps on the C64", "B. Ram"));
        toReturn.add(CalendarEntry.getDummyEntry(10, 16, 18, 30, "Programming with WebOS", "LG"));
        toReturn.add(CalendarEntry.getDummyEntry(10, 16, 8, 30, "QT on mobile devices", "RIM"));
        toReturn.add(CalendarEntry.getDummyEntry(10, 16, 9, 30, "Done with the sass", "Test"));

        return toReturn;
    }

    public void setIsGoingTo(boolean isGoingTo) {
        this.isGoingTo = isGoingTo;
    }
}
