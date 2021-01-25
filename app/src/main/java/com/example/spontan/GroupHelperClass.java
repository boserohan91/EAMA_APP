package com.example.spontan;

public class GroupHelperClass {

    int grpID;
    String groupName;
    String locationName;
    String activityName;

    public GroupHelperClass(int grpID, String groupName, String locationName, String activityName) {
        this.grpID = grpID;
        this.groupName = groupName;
        this.locationName = locationName;
        this.activityName = activityName;
    }

    public int getGrpID() {
        return grpID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getActivityName() {
        return activityName;
    }
}
