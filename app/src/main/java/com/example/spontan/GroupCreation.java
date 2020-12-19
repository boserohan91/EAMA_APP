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

import java.text.DateFormat;
import java.util.Calendar;

public class GroupCreation extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Button createGrp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_group_creation,null);

        Bundle extras = getIntent().getExtras();
        System.out.println("Checking bundle extras inside onCreate");
        if(extras != null){

            EditText locName = view.findViewById(R.id.editTextTextLocationName);
            EditText locAddr = view.findViewById(R.id.editTextTextLocationAddress);
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
}