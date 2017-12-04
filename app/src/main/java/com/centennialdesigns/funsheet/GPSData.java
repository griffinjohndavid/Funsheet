package com.centennialdesigns.funsheet;

import android.content.Context;

import im.delight.android.location.SimpleLocation;

/**
 * Created by WhatWhat on 12/4/2017.
 */

public class GPSData {
    private static GPSData ourInstance;
    private SimpleLocation mLocation;


    public static GPSData getInstance(Context context) {
        if(ourInstance == null){
            ourInstance = new GPSData(context);
        }
        return ourInstance;
    }

    private GPSData(Context context) {

        mLocation = new SimpleLocation(context, true, false, 100);
        if (!mLocation.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(context);
        }

        mLocation.beginUpdates();
    }

    public double getLatitude(){
        return mLocation.getLatitude();
    }

    public double getLongitude() {
        return mLocation.getLongitude();
    }
}
