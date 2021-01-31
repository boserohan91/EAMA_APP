package com.example.spontan;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
    DbHelper myDb;
    ImageView profileImage;
    Uri imageUri;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileImage = view.findViewById(R.id.imageViewProfile);
        profileImage.setImageResource(R.drawable.profile_avatar);
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        if (Constants.isConnected() && Constants.isConnectionFast()){
            StorageReference profileImgRef = storage.getReference().child("profileImage/"+Constants.getUserName());
            try {
                final File localImgFile = File.createTempFile(Constants.getUserName(),"");
                profileImgRef.getFile(localImgFile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localImgFile.getAbsolutePath());
                                profileImage.setImageBitmap(bitmap);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();

            }

        }


        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        TextView editInterestTextClick = view.findViewById(R.id.textViewClickEditInterestList);
        myDb =Constants.getMyDBHelper(getContext()) ;
        editInterestTextClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SelectInterest.class);
                startActivity(intent);
            }
        });
        // retrieve profile data from DB


        // from local DB
        Cursor res = myDb.getFilteredUserData("UserAuth", "username", Constants.getUserName());
        if(res.getCount() == 0) {
            // show message
            // showMessage("Error","Nothing found");
            System.out.println("No data");
            // get from Firebase then

            return;
        }

        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {

            TextView name = (TextView) view.findViewById(R.id.textViewName);
            TextView email = (TextView) view.findViewById(R.id.textViewEmailID);
            TextView contact = (TextView) view.findViewById(R.id.textViewContactNumber);

            name.setText(res.getString(0));
            email.setText(Constants.getUserName());
            contact.setText(res.getString(3));

            buffer.append("username :"+ res.getString(0)+"\n");
            buffer.append("email:"+ res.getString(1)+"\n");
            buffer.append("contact :"+ res.getString(3)+"\n");

        }
        System.out.println(buffer);
    }

    public void chooseImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            profileImage.setImageURI(imageUri);
            uploadImage();
        }
    }

    public void uploadImage(){
        StorageReference profileStorageReference = storageReference.child("profileImage/"+Constants.getUserName());
        profileStorageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Profile Pic uploaded successfully!", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                    }
                });
    }
}
