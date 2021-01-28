package com.example.spontan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class Group extends AppCompatActivity {

    RecyclerView participantRecycler;
    RecyclerView.Adapter adapter;
    ArrayList<ParticipantHelperClass> participantList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_group, null);

        TextView grpDesc = view.findViewById(R.id.descriptionText);
        TextView grpLoc = view.findViewById(R.id.locationText);
        TextView grpDtTime = view.findViewById(R.id.datetimeText);
        TextView grpName = view.findViewById(R.id.textViewGrpName);

        Bundle extras = getIntent().getExtras();
        System.out.println("Checking bundle extras inside onCreate");

        setContentView(view);

        if (extras != null) {

            String desc = extras.getString("grpDescription");
            String location = extras.getString("locationName") + ", " + extras.getString("locationAddress");
            String dtTime = extras.getString("date") + ", " + extras.getString("time");
            String grpNm = "Group: " + extras.getString("activityName");
            grpDesc.setText(desc);
            grpLoc.setText(location);
            grpDtTime.setText(dtTime);
            grpName.setText(grpNm);

            if (extras.getString("groupID") != null) {
                participantRecycler = findViewById(R.id.participantRecycler);
                groupID = extras.getString("groupID");
                participantRecycler(groupID);
            }

        }
        else {


            db.collection("GroupDetails")
                    .whereEqualTo("__name__", groupID)
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
                                    grpDesc.setText(activityName);
                                    grpLoc.setText(locName + ", " + locAddr);
                                    grpDtTime.setText(date + ", " + time);
                                    grpName.setText(groupName);

                                }


                            } else {
                                System.out.println("Error getting documents: " + task.getException());
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }
    }

    private void participantRecycler(String groupID) {

        participantRecycler.setHasFixedSize(true);
        participantRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false ));

        participantList = new ArrayList<>();
        ArrayList<String> userNames = new ArrayList<>();
        adapter = new ParticipantRecViewAdapter(participantList);
        participantRecycler.setAdapter(adapter);
        // get participants from DB and add them to participantList ArrayList
        // Check Network Connection properties from Constants knowledge base and switch to local DB or apply lazy loading of participant images
        Context context = getApplicationContext();
        if (Constants.isConnected()){
            if (Constants.isConnectionFast()){
                //retrieve data normally from Firebase Server
                CollectionReference participantRef = db.collection("Participants");
                Query query = participantRef.whereEqualTo("GroupID", groupID);
                query.get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                System.out.println(document.getId() + " => " + document.getData());
                                String userName = document.getData().get("UserName").toString();
                                System.out.println(userName);
                                userNames.add(userName);
                            }
                            generateParticipantList(userNames);
                        } else {
                            System.out.println("Error getting documents: "+ task.getException());
                        }
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Unable to load participants. NO internet connection!", Toast.LENGTH_SHORT);
                    }
                });

//                userNames.add("abhi@g.com");




            }
            else {
                //retrieve participant names only
                //download images later
            }
        }
        else{
            //retrieve data from local DB
        }
//        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Rohan Bose"));
//        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Lipsa Pradhan"));
//        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Participant Name"));
//        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Participant Name 2"));
//        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Participant Name 3"));


    }

    private void generateParticipantList(ArrayList userNames){

        db.collection("User").whereIn("UserName", userNames)
        .get(Source.SERVER)
        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getId() + " => " + document.getData());
                        String name = document.getData().get("Name").toString();
                        String uname = document.getData().get("UserName").toString();
                        System.out.println(name+" : "+uname);
                        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, name));
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    System.out.println("Error getting documents: "+ task.getException());
                }
            }})
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Unable to load participants. NO internet connection!", Toast.LENGTH_SHORT);
            }
        });


    }
}