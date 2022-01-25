package com.example.erasmushelp.ui.ptnsInterest;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class Spot implements Serializable {
    private String name, tag;
    //private Coords coords;
    private LatLng location;
    private double x, y;

    public Spot() {
        name = "";
    }

    public Spot(double x, double y, String tag) {
        this.x = x;
        this.y = y;
        //location = new LatLng(x, y);
        this.tag = tag;
    }

    public LatLng getLocation() {
        return new LatLng(x, y);
    }

    public void setLocation(LatLng location) {
        this.x = location.latitude;
        this.y = location.longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
