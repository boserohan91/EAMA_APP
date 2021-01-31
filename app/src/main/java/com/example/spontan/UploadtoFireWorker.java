package com.example.spontan;

import android.content.Context;
import android.database.Cursor;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.HashSet;

public class UploadtoFireWorker extends Worker {

    DbHelper mySQLDb;
    Context context;
    FirebaseFirestore db ;
    public UploadtoFireWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        System.out.println("Upload worker called");
        uploadToFire();

        return Result.success();
    }

    public void uploadToFire(){
        mySQLDb = Constants.getMyDBHelper(context);
        Cursor res = mySQLDb.getFilteredData("GroupDetails", "flag", "1");
        if(res.getCount() == 0) {
            // show message
            // showMessage("Error","Nothing found");
            System.out.println("No data");
            return;
        }

        if (Constants.isConnected()){
            while (res.moveToNext()) {
                db = FirebaseFirestore.getInstance();
                HashMap<String,Object> GroupDetails= new HashMap<>();
                String UserId = res.getString(0);
                GroupDetails.put("GroupName", res.getString(2));
                GroupDetails.put("ActivityName", res.getString(3));
                GroupDetails.put("LocName", res.getString(4));
                GroupDetails.put("LocAddr", res.getString(5));
                GroupDetails.put("Date", res.getString(6));
                GroupDetails.put("Time", res.getString(7));
                GroupDetails.put("Lat", res.getString(8));
                GroupDetails.put("Lon", res.getString(9));

                db.collection("GroupDetails")
                        .add(GroupDetails)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String grpID = documentReference.getId();
                        mySQLDb.updateGroupData(UserId, GroupDetails.get( "GroupName").toString(), grpID);
                        System.out.println("Group successfully uploaded to Firebase: "+GroupDetails.get( "GroupName").toString());
                        Toast.makeText(getApplicationContext(), "Created Group '"+GroupDetails.get("GroupName").toString()+"' successfully published online!!",Toast.LENGTH_LONG);
                    }
                });



            }

        }

    }
}
