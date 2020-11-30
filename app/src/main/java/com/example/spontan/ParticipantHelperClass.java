package com.example.spontan;

public class ParticipantHelperClass {

    int participantImage;
    String participantName;

    public ParticipantHelperClass(int participantImage, String participantName) {
        this.participantImage = participantImage;
        this.participantName = participantName;
    }

    public int getParticipantImage() {
        return participantImage;
    }

    public String getParticipantName() {
        return participantName;
    }
}
