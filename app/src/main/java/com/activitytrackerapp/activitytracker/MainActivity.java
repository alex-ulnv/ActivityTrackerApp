package com.activitytrackerapp.activitytracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    public static final String SHARED_PREF = "sharedPrefs";
    public static final String WALKING_THRESHOLD = "walkingThreshold";
    public static final String RUNNING_THRESHOLD = "runningThreshold";

    public static final int STORAGE_PERMISSION_CODE = 1;

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            initialize();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new ObserverFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_observer);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_observer:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ObserverFragment())
                        .commit();
                break;
            case R.id.nav_train:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new TrainFragment())
                        .commit();
                break;
            case R.id.nav_reset:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new ObserverFragment())
                        .commit();
                DataHandler.reset();
                break;
            case R.id.nav_save:
                saveToCSVFiles();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initialize() {
        loadThresholds();
        if (DataHandler.runningThreshold <= 0 || DataHandler.walkingThreshold <= 0) {
            StartUpDialog sud = new StartUpDialog();
            sud.show(getSupportFragmentManager(), "Start Up Dialog");
        }
    }

    public void loadThresholds() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        DataHandler.runningThreshold = sharedPreferences.getInt(RUNNING_THRESHOLD, -1);
        DataHandler.walkingThreshold = sharedPreferences.getInt(WALKING_THRESHOLD, -1);
        Log.d(
                TAG,
                "loadRunningThreshold: Walking: " +
                        DataHandler.walkingThreshold +
                        " Running: " +
                        DataHandler.runningThreshold
        );
    }

    public void setDefaultThresholds() {
        DataHandler.walkingThreshold = App.DEFAULT_WALKING_THRESHOLD;
        DataHandler.runningThreshold = App.DEFAULT_RUNNING_THRESHOLD;
        saveThresholds(DataHandler.walkingThreshold, DataHandler.runningThreshold);
        Toast.makeText(this, "Default thresholds are updated", Toast.LENGTH_SHORT).show();
    }

    public void saveThresholds(int walkingThreshold, int runningThreshold) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(WALKING_THRESHOLD, walkingThreshold);
        editor.putInt(RUNNING_THRESHOLD, runningThreshold);
        editor.commit();
    }

    private void saveToCSVFiles() {
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestStoragePermission();
        }
        else {

            FileHandler outputFile = new FileHandler(App.activitiesCSVFile);
            outputFile.saveToCSV(DataHandler.activities, UserActivity.getHeaders());

            outputFile = new FileHandler(App.stepsCSVFile);
            outputFile.saveToCSV(DataHandler.steps, Step.getHeaders());

            Toast.makeText(this, "Files are saved in " + App.fileDirectory, Toast.LENGTH_SHORT).show();

        }
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("We need you permission to save the CSV files on your device.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    STORAGE_PERMISSION_CODE
                            );
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveToCSVFiles();
            } else {
                Toast.makeText(this, "Cannot save file without the permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
