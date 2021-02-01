package com.example.spontan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RecommendedActivityFragment extends Fragment {

    public ArrayList<String> recommendedList;
    RecyclerView recommendedRecyclerView;
    public ArrayList<String> otherRecActivities = new ArrayList<String>();
    WeatherCondition weatherCondition;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recommended_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //get weather data using OpenWeatherMap API
        getWeatherData();
        //choose among interested activities depending on weather conditions
        // for that map interested activities to weather conditions first
        recommendedRecyclerView  = view.findViewById(R.id.recommendedActivityRecView);



    }

    public void getWeatherData(){

        double latitude = 0.0;
        double longitude = 0.0;

        MyLocationListener myLocationListener = new MyLocationListener(getContext(), getActivity());
        if(myLocationListener.isCanGetLocation()){
            latitude = myLocationListener.getLatitude();
            longitude = myLocationListener.getLongitude();
            System.out.println("Current Latitude: "+latitude);
            System.out.println("Current Longitude: "+longitude);
        } else{
            // get db saved location or user specified value
            System.out.println("Cannot get location!");
        }

        String URL = Constants.owmURL+"q=Dresden,Germany&appid="+Constants.owmAPIKey+"&units=metric";
        //String URL = Constants.owmURL+"lat="+latitude+"&lon="+longitude+"&appid="+Constants.owmAPIKey+"&units=metric";
        new ServerClass().sendGETRequest(getContext(), URL, new ServerResponseCallback() {
            @Override
            public void onJSONResponse(JSONObject jsonObject) {
                weatherCondition = new WeatherCondition();
                try{
                    JSONObject main_object = jsonObject.getJSONObject("main");
                    JSONArray array = jsonObject.getJSONArray("weather");
                    JSONObject weather_object = array.getJSONObject(0);
                    JSONObject wind_object = jsonObject.getJSONObject("wind");

                    weatherCondition.windspeed = wind_object.getDouble("speed");
                    weatherCondition.temp = main_object.getDouble("temp");
                    weatherCondition.main = weather_object.getString("main");


                }catch (JSONException e){
                    e.printStackTrace();
                }

                try{
                    JSONObject snow_object = jsonObject.getJSONObject("snow");
                    weatherCondition.snow1h = snow_object.getDouble("1h");
                    try{
                        weatherCondition.snow3h = snow_object.getDouble("3h");
                    }
                    catch (JSONException e){
                        weatherCondition.snow3h = 0.0;
                        e.printStackTrace();
                    }
                }
                catch (JSONException e){
                    weatherCondition.snow1h = 0.0;
                    weatherCondition.snow3h = 0.0;
                    e.printStackTrace();
                }

                try{
                    JSONObject rain_object = jsonObject.getJSONObject("rain");
                    weatherCondition.rain1h = rain_object.getDouble("1h");
                    try{
                        weatherCondition.rain3h = rain_object.getDouble("3h");
                    }
                    catch (JSONException e){
                        weatherCondition.rain3h = 0.0;
                        e.printStackTrace();
                    }
                }
                catch (JSONException e){
                    weatherCondition.rain1h = 0.0;
                    weatherCondition.rain3h = 0.0;
                    e.printStackTrace();
                }


                System.out.println("Weather Main: "+ weatherCondition.main);
                System.out.println("Temperature: " + weatherCondition.temp);
                System.out.println("Wind Speed: " + weatherCondition.windspeed);
                System.out.println("Snow 1h: " + weatherCondition.snow1h);
                System.out.println("Snow 3h: " + weatherCondition.snow3h);
                System.out.println("Rain 1h: " + weatherCondition.rain1h);
                System.out.println("Rain 3h: " + weatherCondition.rain3h);
                getInterests();


            }

            @Override
            public void onJSONArrayResponse(JSONArray jsonArray) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void recommendedActivityBuilder(ArrayList<String> userInterests){

        System.out.println("Inside recommendedActivityBuilder");

        Set<String> recommendedActivities = new HashSet<>(Constants.activities.keySet());

        System.out.println("All activities: "+recommendedActivities);

        // extract from database all user interests
        //ArrayList<String> userInterests = new ArrayList<String>(recommendedActivities);
        System.out.println("User Interests: "+ userInterests);
        System.out.println("Weather Main condition inside recommendedActivityBuilder: "+weatherCondition.main );

        if (weatherCondition.main.equals("Rain") || weatherCondition.main.equals("Snow")){
            System.out.println("Inside Rain and snow");
            recommendedActivities.remove("Biking");
            recommendedActivities.remove("Hiking");
            recommendedActivities.remove("Badminton");
            recommendedActivities.remove("Basketball");
            recommendedActivities.add("Indoor Badminton");
            Constants.activities.put("Indoor Badminton", "Indoor Badminton Court");
            recommendedActivities.add("Indoor Basketball");
            Constants.activities.put("Indoor Basketball", "Indoor Basketball Court");
            if (weatherCondition.main.equals("Snow")){
                if(weatherCondition.snow3h > 50 || weatherCondition.snow1h > 25){
                    recommendedActivities.remove("Football");
                    recommendedActivities.add("Indoor Football");
                    Constants.activities.put("Indoor Football", "Indoor Football Court");

                }
            }
        }
        if (weatherCondition.main.equals("Clear") || weatherCondition.main.equals("Clouds")){
            if(weatherCondition.snow3h > 50 || weatherCondition.snow1h > 25 || weatherCondition.rain3h > 50 || weatherCondition.rain1h > 25) {
                recommendedActivities.remove("Biking");
                recommendedActivities.remove("Hiking");
                }
            if(weatherCondition.windspeed > 10){
                recommendedActivities.remove("Badminton");
                recommendedActivities.add("Indoor Badminton");
                Constants.activities.put("Indoor Badminton", "Indoor Badminton Court");
            }
            }
        // compare recommendedActivities with InterestList and show only the common of both the sets
        //display other recommended activities too
        System.out.println("Recommended Activities left: "+recommendedActivities);

        recommendedList = new ArrayList<String>();

        if(userInterests.contains("Football"))
            userInterests.add("Indoor Football");
        if(userInterests.contains("Basketball"))
            userInterests.add("Indoor Basketball");
        if( userInterests.contains("Badminton"))
            userInterests.add("Indoor Badminton");

        for (String activity: recommendedActivities){
            System.out.println(activity);
            if (userInterests.contains(activity)){
                recommendedList.add(activity);

                System.out.println("Activity added: "+ activity);
            }
            else{
                otherRecActivities.add(activity);
                System.out.println("Other recommended activity added: "+activity);
            }
        }


        RecommendedRecViewAdapter recommendedRecViewAdapter = new RecommendedRecViewAdapter(getContext(), getActivity() ,recommendedList);
        recommendedRecyclerView.setAdapter(recommendedRecViewAdapter);
        recommendedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        }

        public void getInterests(){

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Interests")
                    .document(Constants.getUserName())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            final ArrayList<String> interestList = (ArrayList<String>) documentSnapshot.getData().get("interests");
                            System.out.println("InterestList: "+interestList);
                            recommendedActivityBuilder(interestList);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Could not retrieve your interests!", Toast.LENGTH_SHORT);
                            //check local db for interests
                        }
                    });

        }


}
