package com.centennialdesigns.funsheet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Card {

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
}
