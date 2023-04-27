package com.example.cosc_326_l4;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.ExifInterface;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private float[] targetLoc = new float[2];

//    1. Checks for location permissions and gracefully fail if not granted
//    2. Assuming location permissions are granted
//          a. make request for users location
//          b. perform Haversine equation on Lat + Long
    public void getLoc() {
//        Return/close if permissions for FINE or COARSE location aren't granted by the user
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            return;
        }

        fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
                .addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double[] lastLoc = new double[2];
                        lastLoc[0] = location.getLatitude();
                        lastLoc[1] = location.getLongitude();
//                FORMULA
//                Radius of the earth (approx 6371 kilometers)
                        final int radii = 6371;
                        double latDistance = Math.toRadians(lastLoc[0] - targetLoc[0]);
                        double longDistance = Math.toRadians(lastLoc[1] - targetLoc[1]);
                        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lastLoc[0])) * Math.cos(Math.toRadians(targetLoc[0])) * Math.sin(longDistance / 2) * Math.sin(longDistance / 2);
                        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
                        double distance = radii * c;
//                Write the distance calculation to the TextView within the UI (activity_main.xml)
                        TextView outView = findViewById(R.id.textview_id);
                        outView.setText("The distance between you and the photo location is: " + String.valueOf(distance) + " km");
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    if (uri != null) {
                        try {
                            InputStream stream = this.getContentResolver().openInputStream(uri);
                            ImageView image = findViewById(R.id.image_id);
                            image.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri));
                            ExifInterface exifInterface = new ExifInterface(stream);
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                exifInterface.getLatLong(targetLoc);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        ImageView findImage = findViewById(R.id.image_id);
        findImage.setOnClickListener(view -> {
//            Android studio thinks this code won't compile but is incorrect
            pickMedia.launch(new PickVisualMediaRequest.Builder().setMediaType((ActivityResultContracts.PickVisualMedia.VisualMediaType) ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
        });

        Button updateBut = findViewById(R.id.button_id);
        updateBut.setOnClickListener(view -> {
            getLoc();
        });

//        Request location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] { Manifest.permission.ACCESS_FINE_LOCATION }, 0);
        }
    }
}
