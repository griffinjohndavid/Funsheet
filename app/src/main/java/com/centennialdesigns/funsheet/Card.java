package com.centennialdesigns.funsheet;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Card implements Parcelable{

    private int id;
    private String title;
    private String description;
    private double latitude;
    private double longitude;
    private List<String> tags;
    private float rating;
    private double distance;

    Card() {}

    Card(int id, String title, String description, float latitude, float longitude, List<String> tags, float rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tags = tags;
        this.rating = rating;
    }

    Card(Parcel parcel) {
        readFromParcel(parcel);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getTags() {
        String result = "";
        for (int i = 0; i < tags.size(); i++) {
            if (i == tags.size() - 1) {
                result += tags.get(i);
            } else {
                result += tags.get(i) + ", ";
            }
        }
        return result;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getDistance() {
        DecimalFormat df = new DecimalFormat("###.##");
        return Double.valueOf(df.format(distance)) + "mi";
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator(){
        public Card createFromParcel(Parcel in){
            return new Card(in);
        }
        public Card[] newArray(int size){
            return new Card[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        //dest.writeList(tags);
        dest.writeFloat(rating);
        dest.writeDouble(distance);
    }

    private void readFromParcel(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        description = parcel.readString();
        latitude = parcel.readDouble();
        longitude = parcel.readDouble();
        //parcel.readList(tags, null);
        rating = parcel.readFloat();
        distance = parcel.readDouble();
    }

}
