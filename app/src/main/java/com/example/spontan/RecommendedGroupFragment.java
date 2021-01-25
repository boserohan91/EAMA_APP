package com.example.spontan;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecommendedGroupFragment extends Fragment {

    public static ArrayList<GroupHelperClass> groupsList = new ArrayList<>();
    RecyclerView groupsRecycler;
    GroupRecViewAdapter groupsAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupsList.clear();
        if(groupsAdapter!=null){
            groupsAdapter.groupList.clear();
            groupsAdapter.notifyDataSetChanged();
        }

        groupsList.add(new GroupHelperClass(1, "Weekend Football", "Zellescher Campus", "Soccer" ));
        groupsList.add(new GroupHelperClass(2, "Friday Night Badminton", "Johannstadt Office", "Badminton" ));
        View view = inflater.inflate(R.layout.recommended_group, container, false);
        //locationsList.add(new LocationsHelperClass("Rohan Basketball","Zellescher Weg", "club"));
        System.out.println("Group List: "+groupsAdapter);
        groupsRecycler = view.findViewById(R.id.recommendedGroupRecView);
        groupsRecycler.setHasFixedSize(true);
        groupsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL , false ));
        groupsAdapter = new GroupRecViewAdapter(groupsList);
        groupsRecycler.setAdapter(groupsAdapter);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
