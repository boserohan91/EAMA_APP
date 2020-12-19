package com.example.spontan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Group extends AppCompatActivity {

    RecyclerView participantRecycler;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        participantRecycler = findViewById(R.id.participantRecycler);

        participantRecycler();
    }

    private void participantRecycler() {

        participantRecycler.setHasFixedSize(true);
        participantRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL , false ));

        ArrayList<ParticipantHelperClass> participantList = new ArrayList<>();
        // get participants from DB and add them to participantList ArrayList
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Rohan Bose"));
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Lipsa Pradhan"));
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Participant Name"));
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Participant Name 2"));
        participantList.add(new ParticipantHelperClass(R.drawable.ic_launcher_background, "Participant Name 3"));

         adapter = new ParticipantRecViewAdapter(participantList);
         participantRecycler.setAdapter(adapter);
    }
}