package com.example.spontan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class GroupCreation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DbHelper myDb;
    Button createGrp;
    EditText  locName, locAddr, grpDesc, actName,  timeText;
    TextView dateText;
    Map<String,Object> GroupDetails;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = Constants.getMyDBHelper(this);
        View view = getLayoutInflater().inflate(R.layout.activity_group_creation,null);
        GroupDetails = new HashMap<>();
        Bundle extras = getIntent().getExtras();
        System.out.println("Checking bundle extras inside onCreate");
        if(extras != null){

            locName = view.findViewById(R.id.editTextTextLocationName);
            locAddr = view.findViewById(R.id.editTextTextLocationAddress);
            System.out.println("Location Name in extras:"+ extras.getString("locationName"));
            System.out.println("Location Address in extras:"+ extras.getString("locationAddress"));
            locName.setText(extras.getString("locationName"));
            locAddr.setText(extras.getString("locationAddress"));
            GroupDetails.put("LocName", extras.getString("locationName"));
            GroupDetails.put("LocAddr", extras.getString("locationAddress"));
            GroupDetails.put("Lat", extras.getDouble("lat"));
            GroupDetails.put("Lon", extras.getDouble("lon"));

        }
        setContentView(view);

        ImageButton dtPickerBtn = (ImageButton) findViewById(R.id.datePickerBtn);
        dtPickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date_picker");
            }
        });

        createGrp = (Button) findViewById(R.id.createBtn);

        createGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save to local DB and open group page
                // background process should sync between local DB and Firebase server
                // pass on data of group fields as Intent extras
                locName = (EditText)findViewById(R.id.editTextTextLocationName);
                locAddr = (EditText)findViewById(R.id.editTextTextLocationAddress);
                grpDesc = (EditText) findViewById(R.id.editTextTextDescription);
                actName = (EditText) findViewById(R.id.editTextTextActivityName);
                dateText = (TextView) findViewById(R.id.dateText);
                timeText = (EditText) findViewById(R.id.timeText);
                //sqllite db
                if (Constants.isConnected()){
                    AddFire(); // firebase db

                }
                else{
                    AddDataGroupDetails(null,1);
                    Toast.makeText(getApplicationContext(), "ALERT: Offline mode enabled! " +
                            "Group created but not visible to others, unless back online!", Toast.LENGTH_LONG).show();
                    open_group(null);
                }


            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        LocationsHelperClass loc = data.
//    }

    public void open_group(String groupID){


        Intent intent = new Intent(this, Group.class);


        // save group on database, generate group ID and put it in intent extras

        intent.putExtra("locationName", locName.getText().toString());
        intent.putExtra("locationAddress", locAddr.getText().toString());
        intent.putExtra("grpDescription", grpDesc.getText().toString());
        intent.putExtra("activityName", actName.getText().toString());
        intent.putExtra("date", dateText.getText().toString());
        intent.putExtra("time", timeText.getText().toString());
        intent.putExtra("groupID", groupID);

        startActivity(intent);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        TextView dttextView = (TextView) findViewById((R.id.dateText));
        dttextView.setText(currentDateString);

    }

    public  void AddDataGroupDetails(String groupID, int flag) {

        String Groupname= actName.getText().toString();
        String desc = grpDesc.getText().toString();
        String loc = locName.getText().toString();
        String locAdd = locAddr.getText().toString();
        String date = dateText.getText().toString();
        String time = timeText.getText().toString();
        boolean isInserted = myDb.insertDataGroupCreation(Constants.getUserName(), groupID,
                Groupname, desc , loc, locAdd, date,time,
                Double.parseDouble(GroupDetails.get("Lat").toString()),
                Double.parseDouble(GroupDetails.get("Lon").toString()), flag  );
        if(isInserted == true)
            Toast.makeText(GroupCreation.this,"Data Inserted",Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(GroupCreation.this,"Data not Inserted",Toast.LENGTH_SHORT).show();

    }
    public void AddFire(){

        GroupDetails.put("GroupName", actName.getText().toString());
        GroupDetails.put("ActivityName", grpDesc.getText().toString());
        GroupDetails.put("Date", dateText.getText().toString());
        GroupDetails.put("Time", timeText.getText().toString());
        //GroupDetails.put("");
        db.collection("users").document("new-city-id").set(GroupDetails);
        db.collection("GroupDetails")
                .add(GroupDetails)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "Snapshot added with ID:" +documentReference.getId()  );
                        System.out.println("Group Ref ID: "+documentReference.getId());
                        AddDataGroupDetails(documentReference.getId(),0);
                        HashMap<String,String> participant = new HashMap<>();
                        participant.put("GroupID",documentReference.getId());
                        participant.put("UserName", Constants.getUserName());
                        db.collection("Participants")
                                .add(participant)
                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        open_group(documentReference.getId());
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                AddDataGroupDetails(null,1);
                Log.w("TAG", "Error");
            }
        });

    }


}