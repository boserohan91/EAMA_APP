package com.example.spontan;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    DbHelper myDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        Cursor res = myDb.getAllData("UserAuth");
        if(res.getCount() == 0) {
            // show message
            // showMessage("Error","Nothing found");
            System.out.println("No data");
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
}
