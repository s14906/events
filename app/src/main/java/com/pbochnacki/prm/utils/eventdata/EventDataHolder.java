package com.pbochnacki.prm.utils.eventdata;

import android.graphics.drawable.Drawable;

import com.pbochnacki.prm.utils.DateUtils;

import java.util.Date;

public class EventDataHolder implements Comparable<EventDataHolder> {
    private String eventId;
    private String eventName;
    private String eventLocation;
    private String eventDate;
    private String eventImageName;
    private String eventDescription;
    private String eventUserAdded;
    private Drawable eventImage;

    public Drawable getEventImage() {
        return eventImage;
    }

    public void setEventImage(Drawable eventImage) {
        this.eventImage = eventImage;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventImageName() {
        return eventImageName;
    }

    public void setEventImageName(String eventImageName) {
        this.eventImageName = eventImageName;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventUserAdded() {
        return eventUserAdded;
    }

    public void setEventUserAdded(String eventUserAdded) {
        this.eventUserAdded = eventUserAdded;
    }


    @Override
    public int compareTo(EventDataHolder eventDataHolder) {
        Date firstDate = DateUtils.parseDate(getEventDate());
        Date secondDate = DateUtils.parseDate(eventDataHolder.getEventDate());
        if (firstDate.compareTo(secondDate) > 0) {
            return 1;
        } else if (firstDate.compareTo(secondDate) < 1) {
            return -1;
        } else {
            return 0;
        }
    }
}
