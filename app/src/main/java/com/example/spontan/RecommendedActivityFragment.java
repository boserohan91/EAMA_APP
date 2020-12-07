package com.example.spontan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RecommendedActivityFragment extends Fragment {



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
    }

    public void getWeatherData(){
        String URL = Constants.owmURL+"Dresden,Germany&appid="+Constants.owmAPIKey+"&units=imperial";
        new ServerClass().sendGETRequest(getContext(), URL, new ServerResponseCallback() {
            @Override
            public void onJSONResponse(JSONObject jsonObject) {
                try{
                    JSONObject main_object = jsonObject.getJSONObject("main");
                    JSONArray array = jsonObject.getJSONArray("weather");
                    JSONObject weather_object = array.getJSONObject(0);
                    double temp = main_object.getDouble("temp");
                    String main = weather_object.getString("main");

                    double temp_cel = (temp-32)*(double)(5.0/9);
                    // extract from database all user interests
                    // with weather main and temp make a list view of all recommended activities
                    System.out.println("Weather Main: "+ main);
                    System.out.println("Temperature: " + temp_cel);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onJSONArrayResponse(JSONArray jsonArray) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
