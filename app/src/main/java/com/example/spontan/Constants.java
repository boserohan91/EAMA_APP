package com.example.spontan;

import android.content.Context;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

import java.util.HashMap;

public class Constants {

    //OpenWeatherMap API details
    public final static String owmURL = "http://api.openweathermap.org/data/2.5/weather?";
    public final static String owmAPIKey = "cfbfa7020ddaccefb2d198ce3860cc55";

    //Google Places API details
    public final static String placesURL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=";
    public final static String placesAPIKey = "AIzaSyCKg9LOk3ikF3sQB42HRIXGBvHCuZoCZ8o";

    //Hash Map of Activities to nearby searched locations
    public static HashMap<String,String> activities = new HashMap<String,String>() {{
        put("Biking", "Biking");
        put("Hiking", "Hiking");
        put("Football", "Football Court");
        put("Indoor Football", "Indoor Football Court");
        put("Badminton", "Badminton Court");
        put("Indoor Badminton", "Indoor Badminton Court");
        put("Basketball", "Basketball Court");
        put("Indoor Basketball", "Indoor Basketball Court");
        put("Dancing", "Dance Schools");
        put("Swimming", "Swimming Pool");
        put("Movies", "Movie Hall");
        put("Cooking", "Cooking");
        put("Food Explorer", "Restaurants");
        put("Clubbing", "Clubs");
        put("Concerts", "Concerts");
        put("Theatre", "Theatre Hall");
    }};

    public static DbHelper myDBHelper;
    public static boolean connected = true;
    public static boolean connectionFast = true;
    public static boolean batteryLevelLow = false;
    public static String userName;

    public static String name;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Constants.name = name;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        Constants.userName = userName;
    }


    public static boolean isBatteryLevelLow() {
        return batteryLevelLow;
    }

    public static void setBatteryLevelLow(boolean batteryLevelLow) {
        Constants.batteryLevelLow = batteryLevelLow;
    }

    public static void setConnected(boolean connected) {
        Constants.connected = connected;
    }

    public static void setConnectionFast(boolean connectionFast) {
        Constants.connectionFast = connectionFast;
    }

    public static boolean isConnected() {
        return connected;
    }

    public static boolean isConnectionFast() {
        return connectionFast;
    }

    public static void buildActivityHashMap(){
        activities.put("Biking", "Biking");
        activities.put("Hiking", "Hiking");
        activities.put("Football", "Football Court");
        activities.put("Indoor Football", "Indoor Football Court");
        activities.put("Badminton", "Badminton Court");
        activities.put("Indoor Badminton", "Indoor Badminton Court");
        activities.put("Basketball", "Basketball Court");
        activities.put("Indoor Basketball", "Indoor Basketball Court");
        activities.put("Dancing", "Dance Schools");
        activities.put("Swimming", "Swimming Pool");
        activities.put("Movies", "Movie Hall");
        activities.put("Cooking", "Cooking");
        activities.put("Food Explorer", "Restaurants");
        activities.put("Clubbing", "Clubs");
        activities.put("Concerts", "Concerts");
        activities.put("Theatre", "Theatre Hall");
        System.out.println("Interests HashMap built");
    }

    public static boolean isNightModeActive(Context context) {
        int defaultNightMode = AppCompatDelegate.getDefaultNightMode();
        if (defaultNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            return true;
        }
        if (defaultNightMode == AppCompatDelegate.MODE_NIGHT_NO) {
            return false;
        }

        int currentNightMode = context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                return false;
            case Configuration.UI_MODE_NIGHT_YES:
                return true;
            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                return false;
        }
        return false;
    }

    public static DbHelper getMyDBHelper(Context context) {
        if (myDBHelper==null){
            myDBHelper = new DbHelper((context));
        }
        return myDBHelper;
    }
}
