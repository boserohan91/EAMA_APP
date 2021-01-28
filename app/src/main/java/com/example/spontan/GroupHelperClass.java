package com.example.spontan;

public class GroupHelperClass {

    String grpID;
    String groupName;
    String locationName;
    String activityName;
    String locAddr;
    String time;
    String date;
    double lat;
    double lon;

    public GroupHelperClass(String grpID, String groupName, String locationName, String locAddr, String activityName, String date, String time, double lat, double lon) {
        this.grpID = grpID;
        this.groupName = groupName;
        this.locationName = locationName;
        this.activityName = activityName;
        this.locAddr = locAddr;
        this.date=date;
        this.time=time;
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


    public String getLocAddr() {
        return locAddr;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
