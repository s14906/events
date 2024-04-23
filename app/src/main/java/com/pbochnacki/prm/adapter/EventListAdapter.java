package com.pbochnacki.prm.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.pbochnacki.prm.MainActivity;
import com.pbochnacki.prm.R;
import com.pbochnacki.prm.utils.eventdata.EventDataHolder;
import com.pbochnacki.prm.utils.eventdata.EventDataUtils;
import com.pbochnacki.prm.utils.progressdialog.ProgressDialogUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class EventListAdapter extends BaseAdapter {
    private final MainActivity context;
    private static LayoutInflater inflater = null;
    private final List<EventDataHolder> sortedEventDataHolderList;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public EventListAdapter(MainActivity context) {
        this.context = context;
        this.sortedEventDataHolderList = EventDataUtils.getAllEventsSortedByDate();
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sortedEventDataHolderList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewElementHolder holder = new ViewElementHolder();
        View row = inflater.inflate(R.layout.activity_list_element, null);
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        EventDataHolder event = sortedEventDataHolderList.get(position);
        StorageReference fileRef = storageRef.child(String.valueOf(event.getEventImageName()));
        if (event.getEventImage() != null) {
            setEventData(holder, row, event);
            holder.eventImage.setImageDrawable(event.getEventImage());
            holder.eventImage.setDrawingCacheEnabled(true);
        } else {
            try {
                File imageFile = File.createTempFile("image", ".jpg");
                ProgressDialog progressDialog = new ProgressDialog(context);
                downloadEventFromFirebaseStorage(holder, row, event, fileRef, imageFile, progressDialog);
            } catch (IOException e) {
                Log.e("EventListAdapter", "Failed to load image file");
            }
        }
        context.onElementClick(row, sortedEventDataHolderList.get(position));
        return row;
    }

    private void downloadEventFromFirebaseStorage(ViewElementHolder holder, View row, EventDataHolder event, StorageReference fileRef, File imageFile, ProgressDialog progressDialog) {
        fileRef.getFile(imageFile).addOnSuccessListener(taskSnapshot -> {
            setEventData(holder, row, event);
            loadEventPhoto(holder, event, imageFile);
            progressDialog.dismiss();
        }).addOnProgressListener(snapshot -> {
            ProgressDialogUtils.showProgressDialog(progressDialog, snapshot, "Loading event list...");
        });
    }

    private void loadEventPhoto(ViewElementHolder holder, EventDataHolder event, File file) {
        Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.ALL).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                holder.eventImage.setImageDrawable(resource);
                holder.eventImage.setDrawingCacheEnabled(true);
                event.setEventImage(resource);
            }
        });
    }

    private void setEventData(ViewElementHolder holder, View row, EventDataHolder event) {
        holder.eventName = row.findViewById(R.id.eventName);
        holder.eventLocation = row.findViewById(R.id.eventLocation);
        holder.eventDate = row.findViewById(R.id.eventDate);
        holder.eventImage = row.findViewById(R.id.eventPhoto);
        holder.eventName.setText(event.getEventName());
        holder.eventLocation.setText(event.getEventLocation());
        holder.eventDate.setText(event.getEventDate());
        ImageView icon = row.findViewById(R.id.imageView);
        icon.setImageResource(R.drawable.alert_icon);
    }

    public static class ViewElementHolder {
        TextView eventName, eventLocation, eventDate;
        ImageView eventImage;
    }
}
