package com.example.spontan;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecViewAdapter extends RecyclerView.Adapter<RecViewAdapter.ActivityListHolder>  {

    String act_array[];
    Context context;
    private OnItemClickListener mListener;
    ArrayList<String> selectedActivities = new ArrayList<String>();

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public RecViewAdapter(Context ct, String activities[]){
        context = ct;
        act_array = activities;
    }

    @NonNull
    @Override
    public ActivityListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recycler_row, parent,false);
        return new ActivityListHolder(view);
    }

    @Override
    public int getItemCount() {
        return act_array.length;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityListHolder holder, int position) {
        holder.activityText.setText(act_array[position]);
    }

    public class ActivityListHolder extends RecyclerView.ViewHolder {

        TextView activityText;
        CardView activityCard;

        public ActivityListHolder(@NonNull View itemView) {
            super(itemView);
            activityText = itemView.findViewById(R.id.activityText);
            activityCard = (CardView) itemView.findViewById(R.id.activityCard);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            CardView cv = v.findViewById(R.id.activityCard);
                            TextView tv = v.findViewById(R.id.activityText);
                            if(selectedActivities.contains(act_array[position])){
                                cv.setBackground(context.getDrawable(R.drawable.custom_button_off));
                                tv.setTextColor(context.getColor(R.color.textviewgrey));
                                selectedActivities.remove(act_array[position]);
                                Toast.makeText(context, act_array[position] + " removed", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                cv.setBackground(context.getDrawable(R.drawable.custom_button));
                                tv.setTextColor(context.getColor(R.color.white));
                                selectedActivities.add(act_array[position]);
                                Toast.makeText(context, act_array[position] + " selected", Toast.LENGTH_SHORT).show();
                            }

                            mListener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }


}
