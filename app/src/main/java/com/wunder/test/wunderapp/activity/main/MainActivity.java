package com.wunder.test.wunderapp.activity.main;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.wunder.test.wunderapp.database.DatabaseHelper;
import com.wunder.test.wunderapp.R;
import com.wunder.test.wunderapp.model.Car;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String DATA_URL = "https://s3-us-west-2.amazonaws.com/wunderbucket/locations.json";
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<Car> cars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Database helper instance
        databaseHelper = new DatabaseHelper(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        cars = new ArrayList<>();

        // Get data
        getCarsData();
    }

    private void getCarsData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        final Context context = this;

        // Progress dialog
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        // Make request to get data
        // TODO: Create service class to make API calls
        StringRequest request = new StringRequest(StringRequest.Method.GET,
                DATA_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
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

                            // Display car into the list of cars
                            adapter = new CarListAdapter(cars, context);
                            recyclerView.setAdapter(adapter);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Display error if any
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Make request
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
