package com.pbochnacki.prm.db;

public class EventEntity {
    private String eventId;
    private String eventName;
    private String eventLocation;
    private String eventImageName;
    private String eventDate;
    private String eventDescription;
    private String eventUserAdded;

    public String getEventUserAdded() {
        return eventUserAdded;
    }

    public void setEventUserAdded(String eventUserAdded) {
        this.eventUserAdded = eventUserAdded;
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

    public String getEventImageName() {
        return eventImageName;
    }

    public void setEventImageName(String eventImageName) {
        this.eventImageName = eventImageName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
