package com.example.spontan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class Group extends AppCompatActivity {

    RecyclerView participantRecycler;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_group,null);

        Bundle extras = getIntent().getExtras();
        System.out.println("Checking bundle extras inside onCreate");
        if(extras != null){

            TextView grpDesc = view.findViewById(R.id.descriptionText);
            TextView grpLoc = view.findViewById(R.id.locationText);
            TextView grpDtTime = view.findViewById(R.id.datetimeText);
            TextView grpName = view.findViewById(R.id.textViewGrpName);


//            System.out.println("Location Name in extras:"+ extras.getString("locationName"));
//            System.out.println("Location Address in extras:"+ extras.getString("locationAddress"));
            String desc = extras.getString("grpDescription");
            String location = extras.getString("locationName") + ", " + extras.getString("locationAddress");
            String dtTime = extras.getString("date") +  ", " + extras.getString("time");
            String grpNm = "Group: " + extras.getString("activityName");
            grpDesc.setText(desc);
            grpLoc.setText(location);
            grpDtTime.setText(dtTime);
            grpName.setText(grpNm);


        }
        setContentView(view);


        participantRecycler = findViewById(R.id.participantRecycler);

        participantRecycler();
    }

    private void participantRecycler() {

        participantRecycler.setHasFixedSize(true);
        participantRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false ));

        ArrayList<ParticipantHelperClass> participantList = new ArrayList<>();
        // get participants from DB and add them to participantList ArrayList
        // Check Network Connection properties from ConnectivityMonitor class and switch to local DB or apply lazy loading of participant images
        Context context = getApplicationContext();
        if (Constants.isConnected()){
            if (Constants.isConnectionFast()){
                //retrieve data normally from Firebase Server
            }
            else {
                //retrieve participant names only
                //download images later
            }
        }
        else{
            //retrieve data from local DB
        }
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Rohan Bose"));
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Lipsa Pradhan"));
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Participant Name"));
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Participant Name 2"));
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Participant Name 3"));

         adapter = new ParticipantRecViewAdapter(participantList);
         participantRecycler.setAdapter(adapter);
    }
}