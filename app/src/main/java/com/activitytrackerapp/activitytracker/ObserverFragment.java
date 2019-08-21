package com.activitytrackerapp.activitytracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ObserverFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ObserverFragment";

    Button startSensorBttn, stopSensorBttn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_observer, container, false);

        startSensorBttn = view.findViewById(R.id.start_sensor_service);
        stopSensorBttn = view.findViewById(R.id.stop_sensor_service);
        stopSensorBttn.setEnabled(false);

        startSensorBttn.setOnClickListener(this);
        stopSensorBttn.setOnClickListener(this);

        BottomNavigationView bottonNav = view.findViewById(R.id.bottom_navigation);
        bottonNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedFragment = null;
                switch (menuItem.getItemId()) {
                    case R.id.bnav_overview:
                        selectedFragment = new OverviewFragment();
                        break;
                    case R.id.bnav_by_steps:
                        selectedFragment = new ByStepsFragment();
                        break;
                }
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.observer_container, selectedFragment)
                        .commit();
                return true;
            }
        });

        Fragment fragment;

        // Loop through fragment, pre-set necessary data bindings
        fragment = new OverviewFragment();
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.observer_container, fragment)
                .commit();

        // By default, inflate the "By Steps" fragment
        fragment = new ByStepsFragment();
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.observer_container, fragment)
                .commit();
        bottonNav.setSelectedItemId(R.id.bnav_by_steps);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.start_sensor_service:
                startSensorService();
                break;
            case R.id.stop_sensor_service:
                stopSensorService();
                break;
        }
    }

    public void startSensorService() {
        Log.d(TAG, "startSensorService: Starting Sensor Service");
        startSensorBttn.setEnabled(false);
        stopSensorBttn.setEnabled(true);

        Intent serviceIntent = new Intent(getContext(), SensorService.class);
        ContextCompat.startForegroundService(getContext(), serviceIntent);
    }

    public void stopSensorService() {
        Log.d(TAG, "startSensorService: Stopping Sensor Service");
        stopSensorBttn.setEnabled(false);
        startSensorBttn.setEnabled(true);

        Intent serviceIntent = new Intent(getContext(), SensorService.class);
        getContext().stopService(serviceIntent);

        DataHandler.endLastActivity();
    }
}
