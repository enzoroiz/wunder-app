package com.wunder.test.wunderapp.model;

import com.google.android.gms.maps.model.LatLng;

public class Car {
    private long id;
    private String name;
    private String vin;
    private String exterior;
    private String interior;
    private String fuel;
    private String address;
    private String engineType;
    private LatLng location;

    public Car(String name, String vin) {
        this.name = name;
        this.vin = vin;
    }

    public long getId() {
        return id;
    }

    public String getExterior() {
        return exterior;
    }

    public String getInterior() {
        return interior;
    }

    public String getFuel() {
        return fuel;
    }

    public String getAddress() {
        return address;
    }

    public String getEngineType() {
        return engineType;
    }

    public LatLng getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getVin() {
        return vin;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setExterior(String exterior) {
        this.exterior = exterior;
    }

    public void setInterior(String interior) {
        this.interior = interior;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }
}
