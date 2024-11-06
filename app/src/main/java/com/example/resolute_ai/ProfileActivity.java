package com.example.resolute_ai;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        db = FirebaseFirestore.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this); // Pass 'this' to getMapAsync
        }

        // Load user information from Firestore and display it
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Set user's location
        LatLng userLocation = new LatLng(-34, 151); // Replace with actual user location
        mMap.addMarker(new MarkerOptions().position(userLocation).title("You are here"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
    }

    private void loadUserProfile() {
        String userId = "user_id"; // Replace with actual user ID or get it from authentication
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String userName = documentSnapshot.getString("name");
                String userEmail = documentSnapshot.getString("email");
                // Update your UI components with userName and userEmail
            } else {
                // Handle case where user document does not exist
            }
        }).addOnFailureListener(e -> {
            // Handle failure to fetch data
        });
    }
}