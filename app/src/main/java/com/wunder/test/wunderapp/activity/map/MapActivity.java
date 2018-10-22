package com.wunder.test.wunderapp.activity.map;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.wunder.test.wunderapp.database.DatabaseHelper;
import com.wunder.test.wunderapp.R;
import com.wunder.test.wunderapp.model.Car;

import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private DatabaseHelper databaseHelper;
    private long selectedCarId;
    private List<Car> cars;
    private Car selectedCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get data passed by intent
        selectedCarId = getIntent().getLongExtra("SELECTED_CAR", -1);

        // Get all cars from database
        databaseHelper = new DatabaseHelper(this);
        cars = databaseHelper.getCars();
        selectedCar = databaseHelper.getCarById(selectedCarId);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        Marker selectedMarker = null;

        // Add car markers to map
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(car.getLocation())
                    .title(car.getName())
            );

            if (selectedCarId == car.getId()) {
                selectedMarker = marker;
            }
        }

        // Zoom to selected car
        if (selectedCar != null) {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedCar.getLocation(), 17));
            map.animateCamera(CameraUpdateFactory.zoomIn());
            map.animateCamera(CameraUpdateFactory.zoomTo(17), 1000, null);
            selectedMarker.showInfoWindow();
        }
    }
}
