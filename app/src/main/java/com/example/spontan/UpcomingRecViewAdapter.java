package com.example.spontan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UpcomingRecViewAdapter extends RecyclerView.Adapter<UpcomingRecViewAdapter.UpcomingListHolder> {

    ArrayList<GroupHelperClass> upcomingActivities;
    Context context;
    Activity activity;

    public UpcomingRecViewAdapter(ArrayList<GroupHelperClass> upcomingActivities, Context context, Activity activity) {
        this.upcomingActivities = upcomingActivities;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public UpcomingRecViewAdapter.UpcomingListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.upcoming_card_design,parent,false);
        UpcomingRecViewAdapter.UpcomingListHolder groupListHolder = new UpcomingRecViewAdapter.UpcomingListHolder(view);
        return groupListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingRecViewAdapter.UpcomingListHolder holder, int position) {
        GroupHelperClass groupHelperClass = upcomingActivities.get(position);
        holder.groupName.setText(groupHelperClass.getGroupName());
        holder.locationName.setText(groupHelperClass.getLocationName());
        holder.activityName.setText(groupHelperClass.getActivityName());
    }

    @Override
    public int getItemCount() {
        return upcomingActivities.size();
    }

    public class UpcomingListHolder extends RecyclerView.ViewHolder{
        TextView groupName;
        TextView locationName;
        TextView activityName;

        public UpcomingListHolder(@NonNull View itemView) {
            super(itemView);

            groupName = itemView.findViewById(R.id.upcomingGroupName);
            locationName = itemView.findViewById(R.id.upcomingLocationName);
            activityName = itemView.findViewById(R.id.upcomingActivityName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // move to search activity page with list of locations
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        // open group page
                        GroupHelperClass group = upcomingActivities.get(position);
                        open_group(group);

                    }
                }
            });
        }
    }

    public void open_group(GroupHelperClass group){


        Intent intent = new Intent(context, Group.class);


        // save group on database, generate group ID and put it in intent extras

        intent.putExtra("locationName", group.locationName);
        intent.putExtra("locationAddress", group.locAddr);
        intent.putExtra("grpDescription", group.activityName);
        intent.putExtra("activityName",group.groupName);
        intent.putExtra("date", group.date);
        intent.putExtra("time", group.time);
        intent.putExtra("groupID", group.grpID);

        context.startActivity(intent);
    }
}