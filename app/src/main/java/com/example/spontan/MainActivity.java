package com.example.spontan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private Button continueBtn;
    private TextView signInClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BatteryLevelReceiver batteryLevelReceiver = new BatteryLevelReceiver();
        registerReceiver(batteryLevelReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        Constants.buildActivityHashMap();

        continueBtn = (Button) findViewById(R.id.loginBtn);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    public void open_interest_list(){
        Intent intent = new Intent(this, SelectInterest.class);
        startActivity(intent);
    }

    public void open_sign_in(){
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
    }
}