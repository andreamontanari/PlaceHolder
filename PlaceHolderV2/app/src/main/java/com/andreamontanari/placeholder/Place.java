package com.andreamontanari.placeholder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by andreamontanari on 08/07/16.
 */
public class Place {

    private String streetName;
    private double latitude, longitude;
    private String savedOn;
    private String placeComment;

    public Place(String streetName, double latitude, double longitude, String placeComment) {
        this.streetName = streetName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeComment = placeComment;
        DateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String requiredDate = df.format(new Date()).toString();
        this.savedOn = requiredDate;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceComment() {
        return placeComment;
    }

    public void setPlaceComment(String placeComment) {
        this.placeComment = placeComment;
    }

    public String getSavedOn() {
        return savedOn;
    }

    public void setSavedOn(String savedOn) {
        this.savedOn = savedOn;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getLatLng() {
        return latitude + ", " + longitude;
    }
}
