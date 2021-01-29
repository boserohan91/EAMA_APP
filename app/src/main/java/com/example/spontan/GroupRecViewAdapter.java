package com.example.spontan;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupRecViewAdapter extends RecyclerView.Adapter<GroupRecViewAdapter.GroupListHolder> {

    DbHelper myDb;
    ArrayList<GroupHelperClass> groupList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                        GroupHelperClass group = groupList.get(position);
                        HashMap<String, String> participant = new HashMap<>();
                        participant.put("GroupID",group.grpID);
                        System.out.println("User joined group:"+Constants.getUserName());
                        participant.put("UserName", Constants.getUserName());
                        db.collection("Participants")
                                .add(participant)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        Log.d("TAG", "Snapshot added with ID:"  );
                                        AddGroupToSQLite(group, itemView.getContext());
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(itemView.getContext(),"Could not join group! No internet connection available!", Toast.LENGTH_SHORT);
                                Log.w("TAG", "Error");
                            }
                        });
                        Context context = itemView.getContext();
                        Intent intent = new Intent(context, Group.class);
                        intent.putExtra("groupID", group.grpID);
                        intent.putExtra("activity", "RecommendedGroups");
                        context.startActivity(intent);
                    }
                }
            });
        }
    }

    public void AddGroupToSQLite(GroupHelperClass group, Context context){

        myDb =  Constants.getMyDBHelper(context);
        try{
            boolean isInserted = myDb.insertDataGroupCreation(Constants.getUserName(),
                    group.grpID,group.groupName, group.activityName ,
                    group.locationName, group.locAddr, group.date,group.time, group.lat, group.lon, 0  );
            if(isInserted == true)
                Toast.makeText(context,"Data Inserted",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(context,"Data not Inserted",Toast.LENGTH_LONG).show();
        }
        catch(SQLiteConstraintException e){
            e.printStackTrace();
            System.out.println("Group already exists in SQLite DB");
        }

    }
}
