 package com.example.spontan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

 public class SignIn extends AppCompatActivity {
    private TextView signUpClick;
    private Button login;
    EditText username, password;
    String name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
                username = (EditText)findViewById(R.id.editTextTextEmailAddressSignIn);
                password = (EditText)findViewById(R.id.editTextTextPassword);
                checkUserIDExists();
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
        finish();
    }

    public void checkUserIDExists(){
        db.collection("User")
                .whereEqualTo("UserName", username.getText().toString())
                .whereEqualTo("Password", password.getText().toString())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(getApplicationContext(),
                                    "Incorrect credentials. Please enter correct user id or password!",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            for (QueryDocumentSnapshot document: (QuerySnapshot) queryDocumentSnapshots){
                                name = document.getData().get("Name").toString();
                                Constants.setName(name);
                            }
                            open_central();
                            Constants.setUserName(username.getText().toString());

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),
                                "Network issue! Please check if you are online!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}