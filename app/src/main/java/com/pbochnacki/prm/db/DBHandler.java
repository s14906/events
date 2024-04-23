package com.pbochnacki.prm.db;

import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pbochnacki.prm.R;
import com.pbochnacki.prm.utils.eventdata.EventDataHolder;
import com.pbochnacki.prm.utils.eventdata.EventDataUtils;

import java.util.Comparator;
import java.util.Optional;

public class DBHandler {

    private static FirebaseDatabase db;
    private static DatabaseReference dbRef;

    public static void setupEventDatabase(Resources resources) {
        db = FirebaseDatabase.getInstance(resources.getString(R.string.db_url));
        dbRef = db.getReference("EventEntity");
    }

    public static void loadEventsListFromDatabase(Task<DataSnapshot> task) {
        for (DataSnapshot snapshot : task.getResult().getChildren()) {
            EventDataHolder eventDataHolder = new EventDataHolder();
            eventDataHolder.setEventId(String.valueOf(snapshot.child("eventId").getValue()));
            eventDataHolder.setEventName(String.valueOf(snapshot.child("eventName").getValue()));
            eventDataHolder.setEventLocation(String.valueOf(snapshot.child("eventLocation").getValue()));
            eventDataHolder.setEventDate(String.valueOf(snapshot.child("eventDate").getValue()));
            eventDataHolder.setEventImageName(String.valueOf(snapshot.child("eventImageName").getValue()));
            eventDataHolder.setEventDescription(String.valueOf(snapshot.child("eventDescription").getValue()));
            eventDataHolder.setEventUserAdded(String.valueOf(snapshot.child("eventUserAdded").getValue()));
            EventDataUtils.getAllEvents().add(eventDataHolder);
        }
    }

    public static DatabaseReference getDbRef() {
        return dbRef;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void saveEvent(EventEntity entity) {
        int idToIncrement = 0;
        Optional<EventDataHolder> largestIdInDB = EventDataUtils.getAllEvents().stream()
                .max(Comparator.comparing(x -> Integer.valueOf(x.getEventId())));
        if (largestIdInDB.isPresent()) {
            idToIncrement = Integer.parseInt(largestIdInDB.get().getEventId()) + 1;
        }
        entity.setEventId(String.valueOf(idToIncrement));
        DatabaseReference newRef = dbRef.push();
        newRef.setValue(entity);
    }

}
