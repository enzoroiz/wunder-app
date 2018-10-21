package com.wunder.test.wunderapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.android.gms.maps.model.LatLng;
import com.wunder.test.wunderapp.model.Car;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database metadata
    private static final int DATABASE_VERSION = 2;
    public static final String DATABASE = "cars.db";
    public static final String TABLE = "cars";

    // Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ADDRESS = "address";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LNG = "lng";
    public static final String COLUMN_ENGINE_TYPE = "engine_type";
    public static final String COLUMN_EXTERIOR = "exterior";
    public static final String COLUMN_FUEL = "fuel";
    public static final String COLUMN_INTERIOR = "interior";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_VIN = "vin";

    // Column indexes
    public static final int INDEX_ID = 0;
    public static final int INDEX_ADDRESS = 1;
    public static final int INDEX_ENGINE_TYPE = 2;
    public static final int INDEX_EXTERIOR = 3;
    public static final int INDEX_FUEL = 4;
    public static final int INDEX_INTERIOR = 5;
    public static final int INDEX_NAME = 6;
    public static final int INDEX_VIN = 7;
    public static final int INDEX_LAT = 8;
    public static final int INDEX_LNG = 9;

    public DatabaseHelper(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create table
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_ENGINE_TYPE + " TEXT, " +
                COLUMN_EXTERIOR + " TEXT, " +
                COLUMN_FUEL + " TEXT, " +
                COLUMN_INTERIOR+ " TEXT, " +
                COLUMN_NAME + " TEXT NOT NULL, " +
                COLUMN_VIN + " TEXT NOT NULL, " +
                COLUMN_LAT + " REAL, " +
                COLUMN_LNG + " REAL" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // Upgrade table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(sqLiteDatabase);
    }

    public long saveCar(Car car) {
        // Insert car data into table
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ADDRESS, car.getAddress());
        contentValues.put(COLUMN_ENGINE_TYPE, car.getEngineType());
        contentValues.put(COLUMN_EXTERIOR, car.getExterior());
        contentValues.put(COLUMN_INTERIOR, car.getInterior());
        contentValues.put(COLUMN_FUEL, car.getFuel());
        contentValues.put(COLUMN_NAME, car.getName());
        contentValues.put(COLUMN_VIN, car.getVin());
        contentValues.put(COLUMN_LAT, car.getLocation().latitude);
        contentValues.put(COLUMN_LNG, car.getLocation().longitude);

        return database.insert(TABLE, null, contentValues);
    }

    public List<Car> getCars() {
        // Retrieve car data
        List<Car> cars = new ArrayList<>();
        SQLiteDatabase database = this.getWritableDatabase();

        // Select all cars
        Cursor cursor = database.rawQuery("SELECT * FROM " +  TABLE, null);
        while (cursor.moveToNext()) {
            Car car = new Car(cursor.getString(DatabaseHelper.INDEX_NAME), cursor.getString(DatabaseHelper.INDEX_VIN));

            // Set car data
            car.setId(cursor.getInt(DatabaseHelper.INDEX_ID));
            car.setInterior(cursor.getString(DatabaseHelper.INDEX_INTERIOR));
            car.setFuel(cursor.getString(DatabaseHelper.INDEX_FUEL));
            car.setExterior(cursor.getString(DatabaseHelper.INDEX_EXTERIOR));
            car.setEngineType(cursor.getString(DatabaseHelper.INDEX_ENGINE_TYPE));
            car.setAddress(cursor.getString(DatabaseHelper.INDEX_ADDRESS));
            Double lat = cursor.getDouble(DatabaseHelper.INDEX_LAT);
            Double lng = cursor.getDouble(DatabaseHelper.INDEX_LNG);
            LatLng location = new LatLng(lat, lng);
            car.setLocation(location);
            cars.add(car);
        }

        return cars;
    }

    public Car getCarById(long carId) {
        // Retrieve car by id
        Car car = null;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " +  TABLE + " WHERE " + COLUMN_ID + "=" + carId, null);

        if (cursor.moveToFirst()) {
            car = new Car(cursor.getString(DatabaseHelper.INDEX_NAME), cursor.getString(DatabaseHelper.INDEX_VIN));

            // Set car data
            car.setId(cursor.getInt(DatabaseHelper.INDEX_ID));
            car.setInterior(cursor.getString(DatabaseHelper.INDEX_INTERIOR));
            car.setFuel(cursor.getString(DatabaseHelper.INDEX_FUEL));
            car.setExterior(cursor.getString(DatabaseHelper.INDEX_EXTERIOR));
            car.setEngineType(cursor.getString(DatabaseHelper.INDEX_ENGINE_TYPE));
            car.setAddress(cursor.getString(DatabaseHelper.INDEX_ADDRESS));
            Double lat = cursor.getDouble(DatabaseHelper.INDEX_LAT);
            Double lng = cursor.getDouble(DatabaseHelper.INDEX_LNG);
            LatLng location = new LatLng(lat, lng);
            car.setLocation(location);
        }

        return car;
    }
}
