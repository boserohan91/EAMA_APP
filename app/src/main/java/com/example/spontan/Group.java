package com.example.spontan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.*;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Group extends AppCompatActivity {

    RecyclerView participantRecycler;
    RecyclerView.Adapter adapter;
    ArrayList<ParticipantHelperClass> participantList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String groupID;
    DbHelper mySQLDb;

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

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
        String activity = " ";
        activity = extras.getString("activity");
        System.out.println("Previous Activity: "+activity);
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
            participantRecycler = findViewById(R.id.participantRecycler);
            if (extras.getString("groupID") != null) {

                groupID = extras.getString("groupID");
                participantRecycler(groupID);
            }
            else{

                participantList = new ArrayList<>();
                mySQLDb = Constants.getMyDBHelper(getApplicationContext());
                participantRecycler.setHasFixedSize(true);
                participantRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false ));

                Cursor res = mySQLDb.getFilteredUserData("UserAuth", "username", Constants.getUserName());
                if(res.getCount() == 0) {
                    // show message
                    // showMessage("Error","Nothing found");
                    System.out.println("No User data");
                    return;
                }
                while (res.moveToNext()) {

                    participantList.add(new ParticipantHelperClass(BitmapFactory.decodeFile(getURLForResource(R.drawable.profile_avatar)),
                            res.getString(0),
                            res.getString(1)));

                }


                adapter = new ParticipantRecViewAdapter(participantList);
                participantRecycler.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
            }

        }
        if(activity!=null){
            if (activity.equals("RecommendedGroups")) {


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
                        Toast.makeText(getApplicationContext(), "Unable to load participants. NO internet connection!", Toast.LENGTH_SHORT).show();
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
                        participantList.add(new ParticipantHelperClass(null, name, uname));
                    }
                    adapter.notifyDataSetChanged();
                    downloadProfileImages(participantList);
                } else {
                    System.out.println("Error getting documents: "+ task.getException());
                }
            }})
                .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Unable to load participants. NO internet connection!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void downloadProfileImages(ArrayList<ParticipantHelperClass> participantList){
        if (Constants.isConnected() && Constants.isConnectionFast()){

            for(ParticipantHelperClass participant: participantList){
                StorageReference profileImgRef = FirebaseStorage.getInstance()
                        .getReference()
                        .child("profileImage/"+participant.getParticipantUserName());
                try {
                    final File localImgFile = File.createTempFile(Constants.getUserName(),"");
                    profileImgRef.getFile(localImgFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localImgFile.getAbsolutePath());
                                    participant.participantImage = bitmap;
                                    adapter.notifyDataSetChanged();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();

                }

            }


        }


    }

    public String getURLForResource (int resourceId) {
        //use BuildConfig.APPLICATION_ID instead of R.class.getPackage().getName() if both are not same
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }
}