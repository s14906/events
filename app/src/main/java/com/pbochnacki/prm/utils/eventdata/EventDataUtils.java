package com.pbochnacki.prm.utils.eventdata;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.pbochnacki.prm.utils.exception.EventException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventDataUtils {
    private static List<EventDataHolder> allEvents = new ArrayList<>();

    public static List<EventDataHolder> getAllEvents() {
        return allEvents;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<EventDataHolder> getAllEventsSortedByDate(){
        return allEvents.stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public static void setAllEvents(List<EventDataHolder> allEvents) {
        EventDataUtils.allEvents = allEvents;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static EventDataHolder getEventById(String eventId) throws EventException {
        if (!allEvents.isEmpty()) {
            Optional<EventDataHolder> eventOptional = allEvents.stream().filter(event -> event.getEventId().equals(eventId)).findAny();
            if (eventOptional.isPresent()) {
                return eventOptional.get();
            } else {
                throw new EventException("Could not find any event by ID: " + eventId);
            }
        } else {
            throw new EventException("Event list is empty, could not find any event by ID: " + eventId);
        }
    }
}
