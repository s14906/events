package com.pbochnacki.prm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.pbochnacki.prm.databinding.ActivityEventDisplayBinding;
import com.pbochnacki.prm.utils.BundleUtils;
import com.pbochnacki.prm.utils.exception.EventException;
import com.pbochnacki.prm.utils.eventdata.EventDataHolder;
import com.pbochnacki.prm.utils.eventdata.EventDataUtils;
import com.pbochnacki.prm.utils.toast.ToastMessageHandler;

public class EventDisplayActivity extends Activity {
    private ActivityEventDisplayBinding binding;
    private Bundle bundle;
    private EventDataHolder event;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventDisplayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                event = EventDataUtils.getEventById(bundle.getString("eventId"));
                binding.eventNameTextViewValue.setText(event.getEventName());
                binding.eventDateTextViewValue.setText(event.getEventDate());
                binding.eventLocationTextViewValue.setText(event.getEventLocation());
                binding.eventDescriptionTextViewValue.setText(event.getEventDescription());
                binding.eventUserAddedTextViewValue.setText(event.getEventUserAdded());
                binding.eventImage.setImageDrawable(event.getEventImage());
                binding.eventImage.setDrawingCacheEnabled(true);

                setupBackButton();
                binding.showMapButton.setOnClickListener(view -> {
                    Intent intent = new Intent(view.getContext(), MapsActivity.class);
                    BundleUtils.passSelectedEventDataToIntent(intent, bundle);
                    startActivity(intent);
                });
            } catch (EventException e) {
                ToastMessageHandler.displayToastMessage(this, "Failed to load the event.");
                goToPreviousScreen(this);
            }
        } else {
            ToastMessageHandler.displayToastMessage(this, "Failed to load the event.");
            goToPreviousScreen(this);
        }

    }

    private void setupBackButton() {
        binding.backButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    private void goToPreviousScreen(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }
}
