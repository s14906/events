package com.pbochnacki.prm.utils.progressdialog;

import android.app.ProgressDialog;

import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.UploadTask;

public class ProgressDialogUtils {

    public static <T> void showProgressDialog(ProgressDialog progressDialog, T snapshot, String message) {
        progressDialog.setTitle(message);
        progressDialog.show();
        if (snapshot instanceof FileDownloadTask.TaskSnapshot) {
            double progress
                    = (100.0
                    * ((FileDownloadTask.TaskSnapshot) snapshot).getBytesTransferred()
                    / ((FileDownloadTask.TaskSnapshot) snapshot).getTotalByteCount());
            progressDialog.setMessage(
                    "Progress "
                            + (int)progress + "%");
        } else {
            double progress
                    = (100.0
                    * ((UploadTask.TaskSnapshot) snapshot).getBytesTransferred()
                    / ((UploadTask.TaskSnapshot) snapshot).getTotalByteCount());
            progressDialog.setMessage(
                    "Progress "
                            + (int)progress + "%");
        }
    }
}

