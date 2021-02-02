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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupRecViewAdapter extends RecyclerView.Adapter<GroupRecViewAdapter.GroupListHolder> {

    DbHelper myDb;
    ArrayList<GroupHelperClass> groupList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Context recGroupContext;
    public GroupRecViewAdapter(ArrayList<GroupHelperClass> groupList, Context context){
        this.groupList = groupList;
        recGroupContext = context;
    }


    @NonNull
    @Override
    public GroupRecViewAdapter.GroupListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rec_group_card_design,parent,false);
        GroupRecViewAdapter.GroupListHolder groupListHolder = new GroupRecViewAdapter.GroupListHolder(view);
        return groupListHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupRecViewAdapter.GroupListHolder holder, int position) {
        GroupHelperClass groupHelperClass = groupList.get(position);
        holder.groupName.setText(groupHelperClass.getGroupName());
        holder.locationName.setText(groupHelperClass.getLocationName());
        holder.locationAddress.setText(groupHelperClass.getLocAddr());
        holder.activityName.setText(groupHelperClass.getActivityName());
        holder.dateTime.setText(groupHelperClass.getDate()+" "+groupHelperClass.getTime());
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public class GroupListHolder extends RecyclerView.ViewHolder {

        TextView groupName;
        TextView locationName;
        TextView activityName;
        TextView locationAddress;
        TextView dateTime;
        ImageButton grpJoinBtn;

        public GroupListHolder(@NonNull View itemView) {
            super(itemView);

            locationName = itemView.findViewById(R.id.recGrplocationName);
            groupName = itemView.findViewById(R.id.recGrpGroupName);
            activityName = itemView.findViewById(R.id.recGrpActivityName);
            locationAddress = itemView.findViewById(R.id.recGrplocationAddress);
            grpJoinBtn = itemView.findViewById(R.id.recGrpJoinBtn);
            dateTime = itemView.findViewById(R.id.recGrpDateTime);

            grpJoinBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        GroupHelperClass group = groupList.get(position);
                        checkUserisParticipantElseJoin(group, v.getContext());
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
            
        }
        catch(SQLiteConstraintException e){
            e.printStackTrace();
            System.out.println("Group already exists in SQLite DB");
        }

    }

    public void checkUserisParticipantElseJoin(GroupHelperClass group, Context context) {
        db.collection("Participants")
                .whereEqualTo("UserName", Constants.getUserName())
                .whereEqualTo("GroupID",group.grpID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.isEmpty()){
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
                                            AddGroupToSQLite(group, context);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context,"Could not join group! No internet connection available!", Toast.LENGTH_SHORT).show();
                                    Log.w("TAG", "Error");
                                }
                            });

                            Intent intent = new Intent(context, Group.class);
                            intent.putExtra("groupID", group.grpID);
                            intent.putExtra("activity", "RecommendedGroups");
                            context.startActivity(intent);
                        }
                        else{
                            Toast.makeText(recGroupContext, "You are already a participant! Please check Upcoming Activities", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(recGroupContext, "No Internet Connection. Please retry again later!", Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
