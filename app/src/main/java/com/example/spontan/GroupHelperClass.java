package com.example.spontan;

public class GroupHelperClass {

    String grpID;
    String groupName;
    String locationName;
    String activityName;
    double lat;
    double lon;

    public GroupHelperClass(String grpID, String groupName, String locationName, String activityName, double lat, double lon) {
        this.grpID = grpID;
        this.groupName = groupName;
        this.locationName = locationName;
        this.activityName = activityName;
        this.lat = lat;
        this.lon = lon;
    }

    public String getGrpID() {
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
