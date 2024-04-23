package com.pbochnacki.prm.utils;

import android.content.Intent;
import android.os.Bundle;

import com.pbochnacki.prm.utils.eventdata.EventDataHolder;

public class BundleUtils {
    public static void passSelectedEventDataToIntent(Intent intent, EventDataHolder eventDataHolder) {
        Bundle bundle = new Bundle();
        bundle.putString("eventId", eventDataHolder.getEventId());
        intent.putExtras(bundle);
    }

    public static void passSelectedEventDataToIntent(Intent intent, Bundle bundle) {
        intent.putExtras(bundle);
    }

    public static void passUsernameToIntent(Intent intent, String username) {
        Bundle bundle = new Bundle();
        bundle.putString("eventUserAdded", username);
        intent.putExtras(bundle);
    }
}
