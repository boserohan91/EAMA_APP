package com.example.spontan;

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

}
