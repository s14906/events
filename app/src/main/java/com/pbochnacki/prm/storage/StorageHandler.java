package com.pbochnacki.prm.storage;

import android.app.ProgressDialog;
import android.net.Uri;
import android.view.View;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pbochnacki.prm.EventAddActivity;
import com.pbochnacki.prm.utils.RandomImageNameGenerator;
import com.pbochnacki.prm.utils.eventdata.EventDataUtils;
import com.pbochnacki.prm.utils.progressdialog.ProgressDialogUtils;

public class StorageHandler {
    public static void uploadEventToFirebaseStorage(View view, Uri imageFilePath, EventAddActivity activity) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference ref = storageRef.child("images/" + RandomImageNameGenerator.getImageFileName());
        ProgressDialog progressDialog
                = new ProgressDialog(activity);
        ref.putFile(imageFilePath).addOnSuccessListener(taskSnapshot -> {
            EventDataUtils.getAllEvents().clear();
            progressDialog.dismiss();
            activity.goToPreviousScreen(view.getContext());
        }).addOnProgressListener(snapshot -> {
            progressDialog.setTitle("Adding event...");
            progressDialog.show();
            double progress
                    = (100.0
                    * snapshot.getBytesTransferred()
                    / snapshot.getTotalByteCount());
            progressDialog.setMessage(
                    "Progress "
                            + (int)progress + "%");

            ProgressDialogUtils.showProgressDialog(progressDialog, snapshot, "Adding new event...");
        });
    }

}
