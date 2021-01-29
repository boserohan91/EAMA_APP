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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivityFragment extends Fragment implements RecommendedActivityText {


    public static ArrayList<LocationsHelperClass> locationsList = new ArrayList<>();
    RecyclerView locationsRecycler;
    LocationsRecViewAdapter locationsAdapter;
    EditText searchActivityEditText;
    String activityText;
    public Button searchActivityBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        locationsList.clear();
        if(locationsAdapter!=null){
            locationsAdapter.locationsList.clear();
            locationsAdapter.notifyDataSetChanged();
        }


        View view = inflater.inflate(R.layout.search_activity, container, false);
        System.out.println("Locations List 1: "+locationsList);
        locationsRecycler = view.findViewById(R.id.locationsRecyclerView);
        locationsRecycler.setHasFixedSize(true);
        locationsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL , false ));
        locationsAdapter = new LocationsRecViewAdapter(locationsList);
        locationsRecycler.setAdapter(locationsAdapter);

        searchActivityEditText = (EditText) view.findViewById(R.id.editTextSearchActivity);
        searchActivityEditText.setText(activityText);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // use places Google API to find places nearby user where searched activity can be performed and return list view
        searchActivityBtn = (Button) view.findViewById(R.id.searchBtn);

        searchActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationsList.clear();
                locationsAdapter.locationsList.clear();

                // use network location provider or gps, based on battery level & connectivity
                getPlaces();

            }
        });

        if(searchActivityEditText != null){
            searchActivityBtn.performClick();
        }
        // also return all the open groups for the activity
    }

    public void getPlaces(){

        searchActivityEditText = (EditText) getView().findViewById(R.id.editTextSearchActivity);
        String searchedActivity = Constants.activities.get(searchActivityEditText.getText().toString());
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
                //System.out.println(jsonObject.toString());
                parseJSONResponse(jsonObject);
            }

            @Override
            public void onJSONArrayResponse(JSONArray jsonArray) {

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    public void parseJSONResponse(JSONObject jsonObject) {
        try{
            JSONArray results = jsonObject.getJSONArray("results");
            for(int i=0;i<results.length();i++){
                JSONObject place_object = results.getJSONObject(i);
                String name = place_object.getString("name");
                String address = place_object.getString("formatted_address");
                String types = place_object.getJSONArray("types").toString();
                JSONObject geometry = place_object.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                double lat = location.getDouble("lat");
                double lon = location.getDouble("lng");
                locationsList.add(new LocationsHelperClass(name, address, types, lat, lon));
                System.out.println("Location Name: "+locationsList.get(i).getLocationName());


            }
        } catch (JSONException e){
            e.printStackTrace();
        }
        finally {
            locationsAdapter.locationsList.addAll(locationsList);

            locationsAdapter.notifyDataSetChanged();
        }


    }


    @Override
    public void setResult(String str) {
        //searchActivityText.setText(str);
        activityText = str;
    }

    @Override
    public void setFragmentResult(Fragment fragment, String str) {
        // leave empty
    }
}
