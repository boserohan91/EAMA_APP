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

public class SearchActivityFragment extends Fragment {


    public static ArrayList<LocationsHelperClass> locationsList = new ArrayList<>();
    ArrayList<LocationsHelperClass> groupsList = new ArrayList<>();
    RecyclerView locationsRecycler;
    RecyclerView groupsRecycler;
    LocationsRecViewAdapter locationsAdapter;
    RecyclerView.Adapter groupsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.search_activity, container, false);
        //locationsList.add(new LocationsHelperClass("Rohan Basketball","Zellescher Weg", "club"));
        System.out.println("Locations List 1: "+locationsList);
        locationsRecycler = view.findViewById(R.id.locationsRecyclerView);
        locationsRecycler.setHasFixedSize(true);
        locationsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL , false ));
        locationsAdapter = new LocationsRecViewAdapter(locationsList);
        locationsRecycler.setAdapter(locationsAdapter);

        Button createGroupBtn = (Button) view.findViewById(R.id.grpCreateBtn);
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GroupCreation.class);
                startActivity(intent);
            }


        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        // use places Google API to find places nearby user where searched activity can be performed and return list view
        Button searchActivityBtn = (Button) view.findViewById(R.id.searchBtn);
        searchActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationsList.clear();
                locationsAdapter.locationsList.clear();

                // use network location provider or gps, based on battery level & connectivity
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
                locationsList.add(new LocationsHelperClass(name, address, types));
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


}
