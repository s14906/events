package com.pbochnacki.prm.utils.toast;

import android.content.Context;
import android.widget.Toast;

public class ToastMessageHandler {
    public static void displayToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
