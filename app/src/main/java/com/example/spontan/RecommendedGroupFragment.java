package com.example.spontan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class RecommendedGroupFragment extends Fragment {

    public static ArrayList<GroupHelperClass> groupsList = new ArrayList<>();
    RecyclerView groupsRecycler;
    GroupRecViewAdapter groupsAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    double latitude;
    double longitude;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupsList.clear();
        if(groupsAdapter!=null){
            groupsAdapter.groupList.clear();
            groupsAdapter.notifyDataSetChanged();
        }
        View view = inflater.inflate(R.layout.recommended_group, container, false);
        //locationsList.add(new LocationsHelperClass("Rohan Basketball","Zellescher Weg", "club"));
        System.out.println("Group List: "+groupsAdapter);
        groupsRecycler = view.findViewById(R.id.recommendedGroupRecView);
        groupsRecycler.setHasFixedSize(true);
        groupsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL , false ));
        groupsAdapter = new GroupRecViewAdapter(groupsList);
        groupsRecycler.setAdapter(groupsAdapter);

//        groupsList.add(new GroupHelperClass(1, "Weekend Football", "Zellescher Campus", "Soccer" ));
//        groupsList.add(new GroupHelperClass(2, "Friday Night Badminton", "Johannstadt Office", "Badminton" ));
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
        double maxLongitude, minLongitude, maxLatitude, minLatitude;
        maxLatitude = latitude + 0.1;
        minLatitude = latitude - 0.1;
        maxLongitude = longitude + 0.1;
        minLongitude = longitude - 0.1;
        CollectionReference groupRef = db.collection("GroupDetails");
        Query query = groupRef.whereLessThanOrEqualTo("Lon", maxLongitude)
                .whereGreaterThanOrEqualTo("Lon", minLongitude);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getId() + " => " + document.getData());
                        String activityName = document.getData().get("ActivityName").toString();
                        String groupName = document.getData().get("GroupName").toString();
                        String locAddr = document.getData().get("LocAddr").toString();
                        String locName = document.getData().get("LocName").toString();
                        String time = document.getData().get("Time").toString();
                        String date = document.getData().get("Date").toString();
                        double lon = Double.parseDouble(document.getData().get("Lon").toString());
                        double lat = Double.parseDouble(document.getData().get("Lat").toString());

                        if (lat<=maxLatitude && lat>=minLatitude){
                            GroupHelperClass group =new GroupHelperClass(document.getId(), groupName, locName, locAddr, activityName, date, time, lat, lon);
                            groupsList.add(group);
                            System.out.println("Group: "+group.groupName);
                        }

                    }
                    groupsAdapter.notifyDataSetChanged();
                } else {
                    System.out.println("Error getting documents: "+ task.getException());
                }
            }
        });




        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
