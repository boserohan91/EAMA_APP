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

public class GroupRecViewAdapter extends RecyclerView.Adapter<GroupRecViewAdapter.GroupListHolder> {

    ArrayList<GroupHelperClass> groupList;

    public GroupRecViewAdapter(ArrayList<GroupHelperClass> groupList){
        this.groupList = groupList;
    }


    @NonNull
    @Override
    public GroupRecViewAdapter.GroupListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.location_card_design,parent,false);
        GroupRecViewAdapter.GroupListHolder groupListHolder = new GroupRecViewAdapter.GroupListHolder(view);
        return groupListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRecViewAdapter.GroupListHolder holder, int position) {
        GroupHelperClass groupHelperClass = groupList.get(position);
        holder.groupName.setText(groupHelperClass.getGroupName());
        holder.locationName.setText(groupHelperClass.getLocationName());
        holder.activityName.setText(groupHelperClass.getActivityName());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public class GroupListHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        TextView locationName;
        TextView activityName;
        ImageButton grpJoinBtn;

        public GroupListHolder(@NonNull View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.locationAddress);
            groupName = itemView.findViewById(R.id.locationName);
            activityName = itemView.findViewById(R.id.locationDescription);
            grpJoinBtn = itemView.findViewById(R.id.locationGrpCreateBtn);

            grpJoinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        GroupHelperClass location = groupList.get(position);
                        Context context = itemView.getContext();
                        Intent intent = new Intent(context, Group.class);
                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}
