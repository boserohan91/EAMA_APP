package com.example.spontan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SelectInterest extends  AppCompatActivity {

    String activities[],  email;
    //ArrayList <String> selectedItems = new ArrayList<String>();
    RecyclerView activityListRecyclerView;
    DbHelper myDb;
    TextView next;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CentralDrawer.class);
        startActivity(intent);
        super.onBackPressed();
    }

    RecViewAdapter recViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interest_list);
        myDb = Constants.getMyDBHelper(this);
        email = Constants.getUserName();

        activityListRecyclerView  = findViewById(R.id.activityrecycleview);

        activities=getResources().getStringArray(R.array.activities);

        recViewAdapter = new RecViewAdapter(this, activities);
        activityListRecyclerView.setAdapter(recViewAdapter);
        activityListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recViewAdapter.setOnItemClickListener(new RecViewAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(int position) {
                //Toast.makeText(SelectInterest.this, activities[position], Toast.LENGTH_SHORT).show();

            }
        });
        next= (TextView)findViewById(R.id.textView11);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recViewAdapter.selectedActivities.size()>=5)
                    for (String selectedItems:recViewAdapter.selectedActivities)
                    {
                        AddDataUserInterest(email, selectedItems);
                        System.out.println(selectedItems);
                        open_central();

                    }
                else{
                    Toast.makeText(SelectInterest.this, "Please select at least 5 activities", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }
    public  void AddDataUserInterest(String email, String interest) {

        boolean isInserted = myDb.insertDataUserInterest(email, interest);
        if(isInserted == true)
            Toast.makeText(SelectInterest.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(SelectInterest.this,"Data not Inserted",Toast.LENGTH_LONG).show();
        myDb.deleteDuplicatesUI("UserInterest");
    }

    public void open_central(){
        Intent intent = new Intent(this, CentralDrawer.class);
        startActivity(intent);
    }



}