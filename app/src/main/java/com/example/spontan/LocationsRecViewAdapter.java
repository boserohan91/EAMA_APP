package com.example.spontan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationsRecViewAdapter extends RecyclerView.Adapter<LocationsRecViewAdapter.LocationsViewHolder> {

    ArrayList<LocationsHelperClass> locationsList;

    public LocationsRecViewAdapter(ArrayList<LocationsHelperClass> locationsList){
        this.locationsList = locationsList;
    }

    @NonNull
    @Override
    public LocationsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        System.out.println("Inside onCreateViewHolder()");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_card_design,parent,false);
        LocationsRecViewAdapter.LocationsViewHolder locationsViewHolder = new LocationsRecViewAdapter.LocationsViewHolder(view);
        return locationsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationsViewHolder holder, int position) {
        System.out.println("Inside onBindViewHolder()");
        LocationsHelperClass locationsHelperClass = locationsList.get(position);
        holder.locationName.setText(locationsHelperClass.getLocationName());
        holder.locationAddress.setText(locationsHelperClass.getLocationAddress());
        holder.locationDescription.setText(locationsHelperClass.getLocationDescription());
    }

    @Override
    public int getItemCount() {
        return locationsList.size();
    }

    public static class LocationsViewHolder extends RecyclerView.ViewHolder{

        TextView locationName;
        TextView locationAddress;
        TextView locationDescription;


        public LocationsViewHolder(@NonNull View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.locationName);
            locationAddress = itemView.findViewById(R.id.locationAddress);
            locationDescription = itemView.findViewById(R.id.locationDescription);
        }
    }
}
