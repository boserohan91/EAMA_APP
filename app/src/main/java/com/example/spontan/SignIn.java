 package com.example.spontan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SignIn extends AppCompatActivity {
    private TextView signUpClick;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        signUpClick = findViewById(R.id.signUp);
        signUpClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_sign_up();
            }
        });

        login = findViewById(R.id.loginBtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // db check
                EditText username = (EditText)findViewById(R.id.editTextTextEmailAddressSignIn);
                Constants.setUserName(username.getText().toString());
                open_central();
            }
        });
    }

    public void open_sign_up(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void open_central(){
        Intent intent = new Intent(this, CentralDrawer.class);
        startActivity(intent);
    }
}