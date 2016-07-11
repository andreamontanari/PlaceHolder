package com.andreamontanari.placeholder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by andreamontanari on 08/07/16.
 */
public class Place extends RealmObject {

    private String streetName;
    private double latitude, longitude;
    private Date savedOn;
    private String placeComment;

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
        DateFormat df = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        String requiredDate = df.format(savedOn).toString();
        return requiredDate;
    }

    public void setSavedOn() {
        this.savedOn = new Date();
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
