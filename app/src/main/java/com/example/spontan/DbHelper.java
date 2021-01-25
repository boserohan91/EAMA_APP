

package com.example.spontan;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "ActivityFinderC.db",null,1);
    }



    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("Create table UserAuth(name Text, username TEXT primary key, password TEXT, contact INTEGER )");
        DB.execSQL("Create table UserInterest(email TEXT , interest TEXT)");
        DB.execSQL("Create table GroupDetails(GroupName TEXT primary key, ActivityName TEXT, LocationName TEXT,LocationAddress TEXT, Date TEXT, Time TEXT, Flag INTEGER)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("drop table if exists UserAuth");
        DB.execSQL("drop table if exists UserInterest");
        DB.execSQL("drop table if exists Groupdetails");
    }

   /* public void AddnewTable(){
        //At first you will need a Database object.Lets create it.
        SQLiteDatabase ourDatabase=this.getWritableDatabase();

        ourDatabase.execSQL("Create table UserInterest(email TEXT , interest TEXT)") ;
    }*/

    public boolean insertDataUserAuth(String name, String username, String password, int contact){

        // userEmail = username;
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("username",username);
        contentValues.put("password", password);
        contentValues.put("contact", contact);
        long result = DB.insert("UserAuth",null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public boolean insertDataUserInterest(String email, String interest){

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email );
        contentValues.put("interest", interest);

        long result = DB.insert("UserInterest",null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public boolean insertDataGroupCreation(String GroupName, String description, String locationName , String  locationAddress,String date, String time, int flag ){

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("GroupName", GroupName );
        contentValues.put("ActivityName", description);
        contentValues.put("LocationName", locationName);
        contentValues.put("LocationAddress", locationAddress);
        contentValues.put("Date", date);
        contentValues.put("Time", time);
        contentValues.put("Flag", flag) ;

        long result = DB.insert("GroupDetails",null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    public void deleteDuplicatesUI( String tableName){

        SQLiteDatabase DB = this.getWritableDatabase();
        DB.execSQL( "DELETE FROM " + tableName + " WHERE rowid NOT IN ( SELECT MIN(rowid) FROM " + tableName + " GROUP BY email, interest)");

    }


    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from UserAuth",null);
        return res;
    }




}

