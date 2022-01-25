package com.example.erasmushelp.ui.ptnsInterest;

import com.google.android.gms.maps.model.LatLng;

public class Coords {
    private double x, y;

    public Coords(double x, double y){
        this.y = y;
        this.x = x;
    }

    public LatLng getLocation(){
        return new LatLng(x, y);
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

    public void setLocation(LatLng location){
        this.x = location.latitude;
        this.y = location.longitude;
    }
}
