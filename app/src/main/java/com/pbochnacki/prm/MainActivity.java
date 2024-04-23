package com.pbochnacki.prm;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.pbochnacki.prm.adapter.EventListAdapter;
import com.pbochnacki.prm.databinding.ActivityMainBinding;
import com.pbochnacki.prm.db.DBHandler;
import com.pbochnacki.prm.utils.eventdata.EventDataHolder;
import com.pbochnacki.prm.utils.BundleUtils;
import com.pbochnacki.prm.utils.eventdata.EventDataUtils;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUserLogin();
        setupMenuToolbar();
        setupAddEventButton();
        DBHandler.setupEventDatabase(getResources());

        DBHandler.getDbRef().get().addOnCompleteListener(task -> {
            if (EventDataUtils.getAllEvents().isEmpty()) {
                DBHandler.loadEventsListFromDatabase(task);
            }
            displayEventListOnScreen();
        });
    }

    private void setupAddEventButton() {
        FloatingActionButton addEventButton = binding.addEventButton;
        addEventButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EventAddActivity.class);
            BundleUtils.passUsernameToIntent(intent, firebaseAuth.getCurrentUser().getDisplayName());
            startActivity(intent);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void displayEventListOnScreen() {
        ListView taskListView = binding.eventList;
        EventListAdapter adapter = new EventListAdapter(this);
        taskListView.setAdapter(adapter);
    }

    private void setupMenuToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu);
        setupSignOutButton(toolbar);
    }

    private void setupSignOutButton(Toolbar toolbar) {
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                signOut();
                return true;
            }
            return false;
        });
    }

    private void signOut() {
        firebaseAuth.signOut();
        mSignInClient.signOut();
        startActivity(new Intent(this, UserSignActivity.class));
        finish();
    }

    private void initUserLogin() {
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            startActivity(new Intent(this, UserSignActivity.class));
            finish();
            return;
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void onElementClick(View row, EventDataHolder eventDataHolder) {
        row.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), EventDisplayActivity.class);
            BundleUtils.passSelectedEventDataToIntent(intent, eventDataHolder);
            startActivity(intent);
        });
    }
}