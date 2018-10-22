package com.wunder.test.wunderapp;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.wunder.test.wunderapp.database.DatabaseHelper;
import com.wunder.test.wunderapp.model.Car;
import com.wunder.test.wunderapp.service.base.HttpService;
import com.wunder.test.wunderapp.service.base.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static org.junit.Assert.*;

public class ServiceUnitTest {
    private final String DATA_URL = "https://s3-us-west-2.amazonaws.com/wunderbucket/locations.json";
    private String TAG = "ServiceUnitTest";
    private DatabaseHelper databaseHelper;
    private List<Car> cars;

    @Mock
    Context context;

    @Before
    public void init() {
        // Set up database
        databaseHelper = new DatabaseHelper(context);
        // Make API request
        HttpService.post(context, DATA_URL, new ResponseListener() {
            @Override
            public void onError(String message) {
                // Display error if any
                Log.e(TAG,"Error while testing the API.");
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray placemarks = json.getJSONArray("placemarks");

                    // For each placemark
                    for (int i = 0; i < placemarks.length(); i++) {
                        JSONObject placemark = placemarks.getJSONObject(i);

                        // Set data to car
                        Car car = new Car(placemark.getString("name"), placemark.getString("vin"));
                        car.setAddress(placemark.getString("address"));
                        car.setEngineType(placemark.getString("engineType"));
                        car.setExterior(placemark.getString("exterior"));
                        car.setFuel(placemark.getString("fuel"));
                        car.setInterior(placemark.getString("interior"));

                        // Set Google Maps coordinates
                        JSONArray coordinates = placemark.getJSONArray("coordinates");
                        LatLng location = new LatLng(coordinates.getDouble(1), coordinates.getDouble(0));
                        car.setLocation(location);

                        // Insert car into database
                        long insertedId = databaseHelper.saveCar(car);
                        if (insertedId != -1) {
                            car.setId(insertedId);
                            cars.add(car);
                        } else {
                            throw new Exception("There was a problem while fetching data, please contact the support team");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Test
    public void checkCanAccessAPI() {
        // TODO Write API and Database tests ...
    }
}