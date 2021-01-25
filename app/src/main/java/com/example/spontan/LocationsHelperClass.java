package com.example.spontan;

public class LocationsHelperClass {

    String locationName;
    String locationAddress;
    String locationDescription;
    double lat;
    double lon;

    public LocationsHelperClass(String locationName, String locationAddress, String locationDescription, double lat, double lon) {
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationDescription = locationDescription;
        this.lat = lat;
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
    public String getLocationName() {
        return locationName;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public String getLocationDescription() {
        return locationDescription;
    }
}
