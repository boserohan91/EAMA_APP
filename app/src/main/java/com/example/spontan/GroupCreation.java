package com.example.spontan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class GroupCreation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    DbHelper myDb;
    Button createGrp;
    EditText  locName, locAddr, grpDesc, actName,  timeText;
    TextView dateText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = Constants.getMyDBHelper(this);
        View view = getLayoutInflater().inflate(R.layout.activity_group_creation,null);

        Bundle extras = getIntent().getExtras();
        System.out.println("Checking bundle extras inside onCreate");
        if(extras != null){

            locName = view.findViewById(R.id.editTextTextLocationName);
            locAddr = view.findViewById(R.id.editTextTextLocationAddress);
            System.out.println("Location Name in extras:"+ extras.getString("locationName"));
            System.out.println("Location Address in extras:"+ extras.getString("locationAddress"));
            locName.setText(extras.getString("locationName"));
            locAddr.setText(extras.getString("locationAddress"));
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
                AddDataGroupDetails();
                open_group();
            }
        });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        LocationsHelperClass loc = data.
//    }

    public void open_group(){


        Intent intent = new Intent(this, Group.class);


        // save group on database, generate group ID and put it in intent extras

        intent.putExtra("locationName", locName.getText().toString());
        intent.putExtra("locationAddress", locAddr.getText().toString());
        intent.putExtra("grpDescription", grpDesc.getText().toString());
        intent.putExtra("activityName", actName.getText().toString());
        intent.putExtra("date", dateText.getText().toString());
        intent.putExtra("time", timeText.getText().toString());

        startActivity(intent);
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

    public  void AddDataGroupDetails() {

        String Groupname= actName.getText().toString();
        String desc = grpDesc.getText().toString();
        String loc = locName.getText().toString();
        String locAdd = locAddr.getText().toString();
        String date = dateText.getText().toString();
        String time = timeText.getText().toString();
        boolean isInserted = myDb.insertDataGroupCreation(Groupname, desc , loc, locAdd, date,time  );
        if(isInserted == true)
            Toast.makeText(GroupCreation.this,"Data Inserted",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(GroupCreation.this,"Data not Inserted",Toast.LENGTH_LONG).show();
    }


}