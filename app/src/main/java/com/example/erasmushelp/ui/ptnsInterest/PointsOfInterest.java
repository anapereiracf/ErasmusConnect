package com.example.erasmushelp.ui.ptnsInterest;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.erasmushelp.R;
import com.example.erasmushelp.data.Consts;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PointsOfInterest extends Fragment implements Consts {
    private DatabaseReference database;

    private GoogleMap googleMap;
    private MapView mapView;

    private RadioButton erasmusSpotsBtn, eatingSpotsBtn, otherSpotsBtn;
    private boolean erasmus = false, restaurants = false, otherSpots = false;

    private final HashMap<Spot, String> spotsMap = new HashMap<>();
    private final List<Marker> erasmusMarkers = new ArrayList<>();
    private final List<Marker> eatingMarkers = new ArrayList<>();
    private final List<Marker> otherMarkers = new ArrayList<>();

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };


    public PointsOfInterest() {

    }

    private void initializeElements(View root, Bundle savedInstanceState) {
        //initialize elements
        mapView = (MapView) root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                }
                googleMap = mMap;

                // For showing zoom controls
                googleMap.getUiSettings().setZoomControlsEnabled(true);

                LatLng caFoscari = new LatLng(45.43451158784037, 12.32632725917972);
                googleMap.addMarker(new MarkerOptions().position(caFoscari).title("Ca' Foscari"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(caFoscari));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(caFoscari).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_points_of_interest, container, false);

        initializeElements(root, savedInstanceState);
        initializeDatabase();
        initializeFilters(root);
        clickListeners(root);
        return root;
    }

    private void initializeFilters(View root) {
        erasmusSpotsBtn = root.findViewById(R.id.erasmusSpots);
        eatingSpotsBtn = root.findViewById(R.id.restaurants);
        otherSpotsBtn = root.findViewById(R.id.otherSpts);
    }

    private void createMarkers() {
        googleMap.clear();
        for (Map.Entry<Spot, String> values : spotsMap.entrySet()) {
            Spot spot = values.getKey();
            MarkerOptions markerOptions = new MarkerOptions().position(spot.getLocation()).title(values.getValue());
            Marker marker = googleMap.addMarker(markerOptions);
            switch (spot.getTag()) {
                case "ErasmusSpot":
                    erasmusMarkers.add(marker);
                    marker.showInfoWindow();
                    marker.hideInfoWindow();
                    marker.setVisible(false);
                    break;
                case "EatingSpot":
                    eatingMarkers.add(marker);
                    marker.showInfoWindow();
                    marker.hideInfoWindow();
                    marker.setVisible(false);
                    break;
                case "OtherSpot":
                    otherMarkers.add(marker);
                    marker.showInfoWindow();
                    marker.hideInfoWindow();
                    marker.setVisible(false);
                    break;
            }
        }
    }

    private void initializeDatabase() {
        database = FirebaseDatabase.getInstance(DATABASE_PATH).getReference().child(PATH_PLACES);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snap : snapshot.getChildren()) {
                        Spot spot = snap.getValue(Spot.class);
                        spotsMap.put(spot, snap.getKey());
                    }
                }
                createMarkers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read failed");
            }
        });
    }

    private void showFilteredMarkers() {
        for (Marker marker : erasmusMarkers)
            marker.setVisible(erasmus);
        for (Marker marker : eatingMarkers)
            marker.setVisible(restaurants);
        for (Marker marker : otherMarkers)
            marker.setVisible(otherSpots);
    }

    private void clickListeners(View root) {
        erasmusSpotsBtn.setOnClickListener(ev -> {
            erasmus = true;
            restaurants = otherSpots = false;

            showFilteredMarkers();
        });

        eatingSpotsBtn.setOnClickListener(ev -> {
            restaurants = true;
            erasmus = otherSpots = false;

            showFilteredMarkers();
        });

        otherSpotsBtn.setOnClickListener(ev -> {
            otherSpots = true;
            erasmus = restaurants = false;

            showFilteredMarkers();
        });
    }
}