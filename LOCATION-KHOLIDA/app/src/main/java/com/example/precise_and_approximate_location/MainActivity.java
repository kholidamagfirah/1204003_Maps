package com.example.precise_and_approximate_location;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnGetApproximateLocation, btnGetPreciseAndApproximateLocation, btnGetPreciseLocation;

    private final String[] approximateLocationPermission = {Manifest.permission.ACCESS_COARSE_LOCATION};
    private final String[] preciseLocationPermission = {Manifest.permission.ACCESS_FINE_LOCATION};
    private final String[] preciseAndApproximateLocationPermissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    private PermissionManager permissionManager;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetApproximateLocation = findViewById(R.id.btnGetApproximateLocation);
        btnGetPreciseLocation = findViewById(R.id.btnGetPreciseLocation);
        btnGetPreciseAndApproximateLocation = findViewById(R.id.btnGetPreciseAndApproximateLocation);

        permissionManager = PermissionManager.getInstance(this);
        locationManager = LocationManager.getInstance(this);

        btnGetApproximateLocation.setOnClickListener(v -> {
            if (!permissionManager.checkPermissions(approximateLocationPermission)) {
                permissionManager.askPermissions(MainActivity.this,
                        approximateLocationPermission, 100);
            } else {
                getLocation();
            }
        });

        btnGetPreciseLocation.setOnClickListener(v -> {
            if (!permissionManager.checkPermissions(preciseLocationPermission)) {
                permissionManager.askPermissions(MainActivity.this,
                        preciseLocationPermission, 200);
            } else {
               getLocation();
            }
        });

        btnGetPreciseAndApproximateLocation.setOnClickListener(v -> {
            if (!permissionManager.checkPreciseAndApproximateLocationPermissions(preciseAndApproximateLocationPermissions)) {
                permissionManager.askPermissions(MainActivity.this,
                        preciseAndApproximateLocationPermissions, 300);
            } else {
                getLocation();
            }
        });
    }

    private void getLocation() {
        if (locationManager.isLocationEnabled()) {
            Location location = locationManager.getLastLocation();
            if (location != null) {
                Toast.makeText(MainActivity.this,
                        "Last Location: \n" + "Lat: " + location.getLatitude() + ", " +
                                "Long: " + location.getLongitude(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Could not fetch location.",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            locationManager.createLocationRequest();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationManager.startLocationUpdates();
    }

    @Override
    protected void onPause() {
        locationManager.stopLocationUpdates();
        super.onPause();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && permissionManager.handleApproximateLocationPermissionResult(grantResults)) {
            locationManager.createLocationRequest();
        } else if (requestCode == 200 && permissionManager.handlePreciseLocationPermissionsResult(grantResults)) {
            locationManager.createLocationRequest();
        } else if (requestCode == 300 && permissionManager.handlePreciseAndApproximateLocationPermissionsResult(grantResults)) {
            locationManager.createLocationRequest();
        }
    }

}