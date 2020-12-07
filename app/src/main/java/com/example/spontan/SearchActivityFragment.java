package com.example.spontan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

public class SearchActivityFragment extends Fragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Button createGroupBtn = (Button) getView().findViewById(R.id.grpCreateBtn);
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupCreation.class);
                startActivity(intent);
            }


        });

        // use places Google API to find places nearby user where searched activity can be performed and return list view
        Button searchActivityBtn = (Button) getView().findViewById(R.id.searchBtn);
        searchActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // use network location provider or gps based based on battery level & connectivity
                getPlaces();
            }
        });

        // also return all the open groups for the activity
    }

    public void getPlaces(){

        EditText searchActivityText = (EditText) getView().findViewById(R.id.editTextSearchActivity);
        String searchedActivity = searchActivityText.getText().toString() + "+court";
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
        String URL = "https://maps.googleapis.com/maps/api/place/textsearch/json?query="+searchedActivity+"&location="+latitude+","+longitude+"&radius=10000&key="+Constants.placesAPIKey;
        new ServerClass().sendGETRequest(getContext(), URL, new ServerResponseCallback() {
            @Override
            public void onJSONResponse(JSONObject jsonObject) {
                System.out.println(jsonObject.toString());
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
