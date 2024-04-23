package com.pbochnacki.prm.utils.eventdata.validator;

import android.graphics.drawable.Drawable;

import com.pbochnacki.prm.utils.DateUtils;
import com.pbochnacki.prm.utils.StringUtils;

public class EventDataValidator {

    public static boolean validateTextInput(String input) {
        if (StringUtils.isNullOrEmpty(input)) {
            return true;
        } else {
            return StringUtils.longerThan(input, 20);
        }
    }

    public static boolean validateDateInput(String eventDate) {
        try {
            DateUtils.parseDate(eventDate);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    public static boolean validateImage(Drawable image) {
        return image == null;
    }
}
