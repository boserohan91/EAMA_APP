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
    public static HashMap<String,String> activities = new HashMap<String,String>();

    public static DbHelper myDBHelper;

    public static void buildActivityHashMap(){
        activities.put("Biking", "Biking");
        activities.put("Hiking", "Hiking");
        activities.put("Football", "Football Court");
        activities.put("Badminton", "Badminton Court");
        activities.put("Basketball", "Basketball Court");
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
