package com.pbochnacki.prm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pbochnacki.prm.databinding.ActivityMapBinding;
import com.pbochnacki.prm.utils.BundleUtils;
import com.pbochnacki.prm.utils.exception.EventException;
import com.pbochnacki.prm.utils.eventdata.EventDataHolder;
import com.pbochnacki.prm.utils.eventdata.EventDataUtils;
import com.pbochnacki.prm.utils.toast.ToastMessageHandler;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ActivityMapBinding binding;
    private Bundle bundle;
    private EventDataHolder event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        bundle = getIntent().getExtras();
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupBackButton();

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            event = EventDataUtils.getEventById(bundle.getString("eventId"));
            try {
                LatLng latLng = getLatLngForEventAddress(geocoder, event.getEventLocation());
                addMarkerToMap(googleMap, latLng);
                addCircleToMap(googleMap, latLng);
                moveCameraToLocation(googleMap, latLng);
            } catch (Exception e) {
                ToastMessageHandler.displayToastMessage(this, "Failed to load the location.");
                goToPreviousScreen(this);
            }
        } catch (EventException e) {
            ToastMessageHandler.displayToastMessage(this, "Failed to load the location.");
            goToPreviousScreen(this);
        }
    }

    private void moveCameraToLocation(GoogleMap googleMap, LatLng latLng) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
    }

    private void addCircleToMap(GoogleMap googleMap, LatLng latLng) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(1000);
        circleOptions.strokeColor(Color.RED);
        circleOptions.strokeWidth(5);
        googleMap.addCircle(circleOptions);
    }

    private void addMarkerToMap(GoogleMap googleMap, LatLng latLng) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title(event.getEventName()));
    }

    @NonNull
    private LatLng getLatLngForEventAddress(Geocoder geocoder, String eventLocation) throws IOException {
        List<Address> addresses = geocoder.getFromLocationName(eventLocation, 1);
        Address address = addresses.get(0);
        double latitude = address.getLatitude();
        double longitude = address.getLongitude();
        return new LatLng(latitude, longitude);
    }


    private void setupBackButton() {
        binding.backButton.setOnClickListener(view -> {
            goToPreviousScreen(view.getContext());
        });
    }

    private void goToPreviousScreen(Context context) {
        Intent intent = new Intent(context, EventDisplayActivity.class);
        BundleUtils.passSelectedEventDataToIntent(intent, bundle);
        startActivity(intent);
    }
}

