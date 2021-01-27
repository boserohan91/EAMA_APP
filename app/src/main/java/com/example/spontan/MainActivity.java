package com.example.spontan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    DbHelper myDb;
    EditText editNamed, editName ,editPass,editContact ;
    Button continueBtn;
    TextView signInClick;
    String email;
    HashMap<String, String> UserDetails;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.deleteDatabase("ActivityFinder.db") ;
        this.deleteDatabase("ActivityFinder1.db");
        this.deleteDatabase("ActivityFinderA.db");
        this.deleteDatabase("ActivityFinderB.db");
        this.deleteDatabase("ActivityFinderC.db");


        editNamed = (EditText)findViewById(R.id.editTextTextEmailAddress2) ;
        editName = (EditText)findViewById(R.id.editTextTextEmailAddress);
        editPass = (EditText)findViewById(R.id.editTextTextPassword);
        editContact = (EditText)findViewById(R.id.editTextPhone);


        continueBtn = (Button) findViewById(R.id.loginBtn);
        myDb = Constants.getMyDBHelper(this);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // myDb.deleteUserInterest("UserInterest");
                System.out.println(editNamed.getText().toString());
                System.out.println(editName.getText().toString());
                System.out.println(editPass.getText().toString());
                System.out.println(editContact.getText().toString());
                AddDataUserAuth();
                AddFire();

                open_interest_list();
            }
        });

        signInClick= (TextView)findViewById(R.id.signIn);
        signInClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_sign_in();
            }
        });

        BatteryLevelReceiver batteryLevelReceiver = new BatteryLevelReceiver();
        registerReceiver(batteryLevelReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        ConnectivityStatusReceiver connectivityStatusReceiver = new ConnectivityStatusReceiver();
        IntentFilter connectionIntentFilter = new IntentFilter();
        connectionIntentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityStatusReceiver, connectionIntentFilter);


        Constants.buildActivityHashMap();


    }

    public void open_interest_list(){
        Intent intent = new Intent(this, SelectInterest.class);
        email =  editName.getText().toString();
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void open_sign_in(){
        Intent intent = new Intent(this, SignIn.class);

        startActivity(intent);
    }

    public  void AddDataUserAuth() {

                        String name= editNamed.getText().toString();
                        String usename = editName.getText().toString();
                        String password =editPass.getText().toString();
                        int contact =Integer.parseInt(editContact.getText().toString()) ;
                        boolean isInserted = myDb.insertDataUserAuth(name, usename , password, contact );
                        if(isInserted == true)
                            Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }

    public void AddFire(){

        Constants.setUserName(editName.getText().toString());
        UserDetails = new HashMap<>();
        UserDetails.put("Name", editNamed.getText().toString());
        UserDetails.put("UserName", editName.getText().toString());


        db.collection("User")
                .add(UserDetails)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "Snapshot added with ID:"  );
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error");
            }
        });
        // TODO
        // user auth updates to Firebase
    }

}