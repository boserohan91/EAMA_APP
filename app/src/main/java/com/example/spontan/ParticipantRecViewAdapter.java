package com.example.spontan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ParticipantRecViewAdapter extends RecyclerView.Adapter<ParticipantRecViewAdapter.ParticipantViewHolder> {

    ArrayList<ParticipantHelperClass> participantList;

    public ParticipantRecViewAdapter(ArrayList<ParticipantHelperClass> participantList){
        this.participantList = participantList;
    }

    @NonNull
    @Override
    public ParticipantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.participant_card_design,parent,false);
        ParticipantViewHolder participantViewHolder = new ParticipantViewHolder(view);
        return participantViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ParticipantViewHolder holder, int position) {
        ParticipantHelperClass participantHelperClass = participantList.get(position);
        holder.participantImage.setImageResource(participantHelperClass.getParticipantImage());
        holder.participantName.setText(participantHelperClass.getParticipantName());
    }

    @Override
    public int getItemCount() {
        return participantList.size() ;
    }

    public static class ParticipantViewHolder extends RecyclerView.ViewHolder{

        ImageView participantImage;
        TextView participantName;

        public ParticipantViewHolder(@NonNull View itemView) {
            super(itemView);

            participantImage = itemView.findViewById(R.id.participantImage);
            participantName = itemView.findViewById(R.id.participantName);
        }
    }
}
