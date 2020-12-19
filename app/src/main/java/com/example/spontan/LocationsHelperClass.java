package com.example.spontan;

public class LocationsHelperClass {

    String locationName;
    String locationAddress;
    String locationDescription;

    public LocationsHelperClass(String locationName, String locationAddress, String locationDescription) {
        this.locationName = locationName;
        this.locationAddress = locationAddress;
        this.locationDescription = locationDescription;
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
