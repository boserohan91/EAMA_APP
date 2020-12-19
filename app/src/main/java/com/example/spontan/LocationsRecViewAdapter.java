package com.example.spontan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

    public class LocationsViewHolder extends RecyclerView.ViewHolder{

        TextView locationName;
        TextView locationAddress;
        TextView locationDescription;
        ImageButton locationGrpCreation;


        public LocationsViewHolder(@NonNull View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.locationName);
            locationAddress = itemView.findViewById(R.id.locationAddress);
            locationDescription = itemView.findViewById(R.id.locationDescription);
            locationGrpCreation = itemView.findViewById(R.id.locationGrpCreateBtn);

            locationGrpCreation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        LocationsHelperClass location = locationsList.get(position);
                        Context context = itemView.getContext();
                        Intent intent = new Intent(context, GroupCreation.class);
                        System.out.println("Selected loc Name: " +location.locationName);
                        System.out.println("Selected loc Address: " +location.locationAddress);
                        intent.putExtra("locationName", location.locationName);
                        intent.putExtra("locationAddress", location.locationAddress);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
