package com.example.spontan;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UpcomingActivityFragment extends Fragment {

    public static ArrayList<GroupHelperClass> upcomingList = new ArrayList<>();
    RecyclerView upcomingRecycler;
    UpcomingRecViewAdapter upcomingAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DbHelper myDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.upcoming_activity, container, false);

        //return all the groups user will participate in the future
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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
                    .get()
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
                            }
                        }
                    });
        }
        else{
            myDb = Constants.getMyDBHelper(getContext());
            Cursor res = myDb.getAllData("GroupDetails");
            if(res.getCount() == 0) {
                // show message
                // showMessage("Error","Nothing found");
                System.out.println("No data");
                return;
            }
            while (res.moveToNext()) {

                upcomingList.add(new GroupHelperClass(res.getString(0),
                        res.getString(1), res.getString(3),
                        res.getString(4), res.getString(2),
                        res.getString(5), res.getString(6),
                        0.0, 0.0));

            }
            upcomingAdapter.notifyDataSetChanged();
        }
    }

    public void generateGroupList(ArrayList groupNames){
        db.collection("GroupDetails").whereIn("__name__", groupNames)
                .get()
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
                                upcomingList.add(new GroupHelperClass(document.getId(), groupName, locName, locAddr, activityName, date, time, lat, lon));
                            }
                            upcomingAdapter.notifyDataSetChanged();
                        } else {
                            System.out.println("Error getting documents: "+ task.getException());
                        }
                    }});

    }
}
