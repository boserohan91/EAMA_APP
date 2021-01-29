package com.example.spontan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    DbHelper myDb;
    EditText editNamed, editUserName ,editPass,editContact ;
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
        this.deleteDatabase("ActivityFinderD.db");
        this.deleteDatabase("ActivityFinderE.db");


        editNamed = (EditText)findViewById(R.id.editTextTextName) ;
        editUserName = (EditText)findViewById(R.id.editTextTextEmailAddress);
        editPass = (EditText)findViewById(R.id.editTextTextPassword);
        editContact = (EditText)findViewById(R.id.editTextPhone);


        continueBtn = (Button) findViewById(R.id.loginBtn);
        myDb = Constants.getMyDBHelper(this);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // myDb.deleteUserInterest("UserInterest");
                System.out.println(editNamed.getText().toString());
                System.out.println(editUserName.getText().toString());
                System.out.println(editPass.getText().toString());
                System.out.println(editContact.getText().toString());
                checkUserIDHasJoinedElseAdd();
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

        PeriodicWorkRequest uploadWorkRequest =
                new PeriodicWorkRequest.Builder(UploadtoFireWorker.class,2, TimeUnit.MINUTES)
                        .build();

        WorkManager.getInstance(getApplicationContext()).enqueueUniquePeriodicWork("periodicUpload", ExistingPeriodicWorkPolicy.KEEP, uploadWorkRequest);
        WorkManager wm = WorkManager.getInstance();

        ListenableFuture<List<WorkInfo>> status = wm.getWorkInfosByTag("periodicUpload");
        try {
            List<WorkInfo> workInfoList = status.get();
            for (WorkInfo info: workInfoList){
                System.out.println(info.getState());
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(status);


    }

    public void open_interest_list(){
        Intent intent = new Intent(this, SelectInterest.class);
        email =  editUserName.getText().toString();
        intent.putExtra("email", email);
        startActivity(intent);
    }

    public void open_sign_in(){
        Intent intent = new Intent(this, SignIn.class);

        startActivity(intent);
    }

    public  void AddDataUserAuth() {

                        String name= editNamed.getText().toString();
                        String usename = editUserName.getText().toString();
                        String password =editPass.getText().toString();
                        int contact =Integer.parseInt(editContact.getText().toString()) ;
                        boolean isInserted = myDb.insertDataUserAuth(name, usename , password, contact );
                        if(isInserted == true)
                            Toast.makeText(MainActivity.this,"Data Inserted",Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(MainActivity.this,"Data not Inserted",Toast.LENGTH_LONG).show();
                    }

    public void AddFire(){


        UserDetails = new HashMap<>();
        UserDetails.put("Name", editNamed.getText().toString());
        UserDetails.put("UserName", editUserName.getText().toString());


        db.collection("User")
                .add(UserDetails)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("TAG", "Snapshot added with ID:"  );
                        Constants.setUserName(editUserName.getText().toString());
                        AddDataUserAuth();
                        open_interest_list();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("TAG", "Error");
                Toast.makeText(getApplicationContext(), "FAILED, Please try again!", Toast.LENGTH_LONG);
            }
        });
        // TODO
        // user auth updates to Firebase
    }

    public void checkUserIDHasJoinedElseAdd(){
        db.collection("User")
                .whereEqualTo("UserName", editUserName.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            AddFire();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),
                                    "User already present with this Email ID! Use different email address!",
                                    Toast.LENGTH_SHORT);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Network issue! Please check if you are online!",
                                Toast.LENGTH_SHORT);
                    }
                });
    }

}