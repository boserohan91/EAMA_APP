package com.example.spontan;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecommendedRecViewAdapter extends RecyclerView.Adapter<RecommendedRecViewAdapter.RecommendedListHolder> {

    ArrayList<String> recommendedActivities;
    Context context;
    Activity activity;

    public RecommendedRecViewAdapter(Context ct, Activity act, ArrayList<String> recommendedActivities) {
        this.context = ct;
        this.activity = act;
        this.recommendedActivities = recommendedActivities;
    }

    @NonNull
    @Override
    public RecommendedListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_row, parent,false);
        return new RecommendedRecViewAdapter.RecommendedListHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedListHolder holder, int position) {
        holder.activityText.setText(recommendedActivities.get(position));
    }

    @Override
    public int getItemCount() {
        return recommendedActivities.size();
    }

    public class RecommendedListHolder extends RecyclerView.ViewHolder{

        TextView activityText;

        public RecommendedListHolder(@NonNull View itemView) {
            super(itemView);
            activityText = itemView.findViewById(R.id.activityText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // move to search activity page with list of locations
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        SearchActivityFragment searchActivityFragment = new SearchActivityFragment();
                        ((RecommendedActivityText)activity).setFragmentResult(searchActivityFragment, activityText.getText().toString());
                        ((CentralDrawer)activity).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchActivityFragment).commit();

                    }
                }
            });
        }
    }
}
