package com.example.spontan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SelectInterest extends  AppCompatActivity {

    String activities[];
    RecyclerView activityListRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_list);

        activityListRecyclerView  = findViewById(R.id.activityrecycleview);

        activities=getResources().getStringArray(R.array.activities);

        RecViewAdapter recViewAdapter = new RecViewAdapter(this, activities);
        activityListRecyclerView.setAdapter(recViewAdapter);
        activityListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recViewAdapter.setOnItemClickListener(new RecViewAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
                //Toast.makeText(SelectInterest.this, activities[position], Toast.LENGTH_SHORT).show();
            }
        });
    }
}