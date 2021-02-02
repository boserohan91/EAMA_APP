package com.example.spontan;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class UpcomingActivityFragment extends Fragment {

    public static ArrayList<GroupHelperClass> upcomingList;
    RecyclerView upcomingRecycler;
    UpcomingRecViewAdapter upcomingAdapter;
    FirebaseFirestore db;
    DbHelper myDb;
    ProgressBar spinnerBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upcoming_activity, container, false);

        //return all the groups user will participate in the future
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

//        upcomingList.clear();
//        if(locationsAdapter!=null){
//            locationsAdapter.locationsList.clear();
//            locationsAdapter.notifyDataSetChanged();
//        }
        upcomingList = new ArrayList<>();
        spinnerBar = (ProgressBar) view.findViewById(R.id.progressBarUpcoming);
        spinnerBar.setVisibility(View.VISIBLE);
        db = FirebaseFirestore.getInstance();
        upcomingRecycler = view.findViewById(R.id.upcomingRecyclerView);
        upcomingRecycler.setHasFixedSize(true);
        upcomingRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL , false ));

        upcomingList = new ArrayList<>();
        ArrayList<String> userNames = new ArrayList<>();
        upcomingAdapter = new UpcomingRecViewAdapter(upcomingList, view.getContext(), getActivity());
        upcomingRecycler.setAdapter(upcomingAdapter);

        if (Constants.isConnected()){
            ArrayList<String> groupNames = new ArrayList<>();
            db.collection("Participants")
                    .whereEqualTo("UserName", Constants.getUserName())
                    .get(Source.SERVER)
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    System.out.println(document.getId() + " => " + document.getData());
                                    String groupName = document.getData().get("GroupID").toString();
                                    System.out.println(groupName);
                                    groupNames.add(groupName);
                                }
                                if(!groupNames.isEmpty())
                                    generateGroupList(groupNames);
                            } else {
                                System.out.println("Error getting documents: "+ task.getException());
                                System.out.println("Getting data from local DB");
                                getDataFromDB();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    System.out.println("Getting data from local DB");
                    getDataFromDB();
                }
            });
        }
        else{
            System.out.println("Getting data from local DB");
            getDataFromDB();
        }
    }

    public void generateGroupList(ArrayList groupNames){
        db.collection("GroupDetails").whereIn("__name__", groupNames)
                .get(Source.SERVER)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                String activityName = document.getData().get("ActivityName").toString();
                                String groupName = document.getData().get("GroupName").toString();
                                String locName = document.getData().get("LocName").toString();
                                String locAddr = document.getData().get("LocAddr").toString();
                                String date = document.getData().get("Date").toString();
                                String time = document.getData().get("Time").toString();
                                double lat = Double.parseDouble(document.getData().get("Lat").toString());
                                double lon = Double.parseDouble(document.getData().get("Lon").toString());
                                GroupHelperClass group = new GroupHelperClass(document.getId(), groupName, locName, locAddr, activityName, date, time, lat, lon);
                                upcomingList.add(group);
                                checkGroupsNotUploaded(getContext());
                                AddGroupToSQLite(group, getContext());

                            }
                            upcomingAdapter.notifyDataSetChanged();
                            spinnerBar.setVisibility(View.GONE);
                        } else {
                            System.out.println("Error getting documents: "+ task.getException());
                            System.out.println("Getting data from DB");
                            getDataFromDB();
                        }
                    }}).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                System.out.println("Getting data from DB");
                getDataFromDB();

            }
        });

    }

    public void getDataFromDB(){

        myDb = Constants.getMyDBHelper(getContext());
        myDb.deleteDuplicatesGroupDetails("GroupDetails");
        Cursor res = myDb.getFilteredData("GroupDetails", "UserId", Constants.getUserName());
        if(res.getCount() == 0) {
            // show message
            // showMessage("Error","Nothing found");
            Toast.makeText(getContext(), "Unable to retrieve upcoming activities!", Toast.LENGTH_SHORT).show();
            spinnerBar.setVisibility(View.GONE);
            System.out.println("No data");
            return;
        }
        while (res.moveToNext()) {
            if(res.getString(10).equals("1")){
                upcomingList.add(new GroupHelperClass(res.getString(1),
                        res.getString(2)+" (not yet uploaded)", res.getString(4),
                        res.getString(5), res.getString(3),
                        res.getString(6), res.getString(7),
                        0.0, 0.0));
            }
            else{
                upcomingList.add(new GroupHelperClass(res.getString(1),
                        res.getString(2), res.getString(4),
                        res.getString(5), res.getString(3),
                        res.getString(6), res.getString(7),
                        0.0, 0.0));
            }


        }
        upcomingAdapter.notifyDataSetChanged();
        spinnerBar.setVisibility(View.GONE);
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

    public void checkGroupsNotUploaded(Context context){
        myDb = Constants.getMyDBHelper(context);
        Cursor res = myDb.getFilteredData("GroupDetails", "flag", "1");
        if(res.getCount() == 0) {
            // show message
            // showMessage("Error","Nothing found");
            System.out.println("No data");
            return;
        }
        while (res.moveToNext()) {
            if (Constants.getUserName().equals(res.getString(0))){
                upcomingList.add(new GroupHelperClass(res.getString(1),
                        res.getString(2)+"(not yet uploaded)", res.getString(4),
                        res.getString(5), res.getString(3),
                        res.getString(6), res.getString(7),
                        0.0, 0.0));
            }

            upcomingAdapter.notifyDataSetChanged();

        }
    }
}
