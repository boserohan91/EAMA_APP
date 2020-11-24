package com.example.spontan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

public class SelectInterests extends  AppCompatActivity {

    String activities[];
    RecyclerView activityListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_list);

        //activityListRecyclerView  = findViewById(R.id.activityrecycleview);

        //activities=getResources().getStringArray(R.array.Activities);

        //RecViewAdapter recViewAdapter = new RecViewAdapter(this, activities);
        //activityListRecyclerView.setAdapter(recViewAdapter);
        //activityListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
