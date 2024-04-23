package com.pbochnacki.prm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.pbochnacki.prm.databinding.ActivityEventAddBinding;
import com.pbochnacki.prm.db.DBHandler;
import com.pbochnacki.prm.db.EventEntity;
import com.pbochnacki.prm.storage.StorageHandler;
import com.pbochnacki.prm.utils.DateUtils;
import com.pbochnacki.prm.utils.RandomImageNameGenerator;
import com.pbochnacki.prm.utils.eventdata.validator.EventDataValidator;
import com.pbochnacki.prm.utils.toast.ToastMessageHandler;

import java.util.Calendar;

public class EventAddActivity extends Activity {

    private ActivityEventAddBinding binding;
    private EditText eventDateTextField;
    private final Calendar cal = Calendar.getInstance();
    private static int SELECT_IMAGE = 200;
    private EventEntity entity;
    private Uri imageFilePath;
    private Bundle bundle;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bundle = getIntent().getExtras();
        setupBackButton();
        setupEventDatePicker();
        setupSaveEventButton();
        setupSelectImageButton();
    }

    private void setupSelectImageButton() {
        Button selectImageButton = binding.selectImageButton;
        selectImageButton.setOnClickListener(view -> {
            Intent imageSelectIntent = new Intent();
            imageSelectIntent.setType("image/*");
            imageSelectIntent.setAction(Intent.ACTION_GET_CONTENT);
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "");

            startActivityForResult(Intent.createChooser(imageSelectIntent, "Select image"), SELECT_IMAGE);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setupSaveEventButton() {
        Button saveEventButton = binding.saveEventButton;
        saveEventButton.setOnClickListener(view -> {
            String eventName = binding.eventNameTextField.getText().toString();
            String eventLocation = binding.eventLocationTextField.getText().toString();
            String eventDate = binding.eventDateTextField.getText().toString();
            String eventDescription = binding.eventDescriptionTextField.getText().toString();
            Drawable eventImage = binding.eventImage.getDrawable();

            if (EventDataValidator.validateTextInput(eventName)) {
                ToastMessageHandler.displayToastMessage(this, "Event name is empty or too long!");
                return;
            }
            if (EventDataValidator.validateTextInput(eventLocation)) {
                ToastMessageHandler.displayToastMessage(this, "Event location is empty or too long!");
                return;
            }
            if (EventDataValidator.validateDateInput(eventDate)) {
                ToastMessageHandler.displayToastMessage(this, "Wrong date entered! Use the date picker.");
                return;
            }
            if (EventDataValidator.validateTextInput(eventDescription)) {
                ToastMessageHandler.displayToastMessage(this, "Event description is empty or too long!");
                return;
            }
            if (EventDataValidator.validateImage(eventImage)) {
                ToastMessageHandler.displayToastMessage(this, "No event image added!");
                return;
            }

            if (entity == null) {
                entity = new EventEntity();
            }
            entity.setEventName(eventName);
            entity.setEventLocation(eventLocation);
            entity.setEventDate(eventDate);
            entity.setEventDescription(eventDescription);
            entity.setEventUserAdded(bundle.getString("eventUserAdded"));

            DBHandler.saveEvent(entity);

            StorageHandler.uploadEventToFirebaseStorage(view, imageFilePath, this);

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_IMAGE) {
                imageFilePath = data.getData();
                RandomImageNameGenerator.generateRandomImageFileName();
                String imageName = "images/" + RandomImageNameGenerator.getImageFileName();
                if (imageFilePath != null) {
                    binding.eventImage.setImageURI(imageFilePath);
                    if (entity == null) {
                        entity = new EventEntity();
                        entity.setEventImageName(imageName);
                    }
                }
            }
        }
    }

    private void setupBackButton() {
        binding.backButton.setOnClickListener(view -> {
            goToPreviousScreen(view.getContext());
        });
    }

    public void goToPreviousScreen(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    private void setupEventDatePicker() {
        eventDateTextField = binding.eventDateTextField;
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            DateUtils.prepareCalendar(cal, year, month, day);
            eventDateTextField.setText(DateUtils.formatDate(cal.getTime()));
        };
        eventDateTextField.setOnClickListener(view ->
                new DatePickerDialog(this,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH))
                        .show());

    }
}
