package com.example.spontan;

import android.graphics.Bitmap;

public class ParticipantHelperClass {

    public Bitmap participantImage;
    String participantName;
    String participantUserName;

    public ParticipantHelperClass(Bitmap participantImage, String participantName, String participantUserName) {
        this.participantImage = participantImage;
        this.participantName = participantName;
        this.participantUserName = participantUserName;
    }

    public Bitmap getParticipantImage() {
        return participantImage;
    }

    public String getParticipantName() {
        return participantName;
    }

    public String getParticipantUserName() {return participantUserName;}
}
