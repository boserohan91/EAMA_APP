package com.example.spontan;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

public class BatteryLevelReceiver extends BroadcastReceiver {

    private static boolean isInAutoDarkMode = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        int batLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        if(batLevel<=20){
            Constants.setBatteryLevelLow(true);
            if (Constants.isNightModeActive(context)){
                // do nothing
            }
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                isInAutoDarkMode = true;
                Toast.makeText(context, "Dark Mode Enabled. Battery Level is Low", Toast.LENGTH_LONG).show();
            }
        }
        else if (batLevel>20 && isInAutoDarkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            isInAutoDarkMode = false;
            Constants.setBatteryLevelLow(false);
            Toast.makeText(context, "Dark Mode automatically Disabled", Toast.LENGTH_LONG).show();
        }
        else{
            Constants.setBatteryLevelLow(false);
        }
    }
}
