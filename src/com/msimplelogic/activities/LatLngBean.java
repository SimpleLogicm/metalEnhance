package com.msimplelogic.activities;

/**
 * Created by sujit on 4/26/2017.
 */

public class LatLngBean {
    String Title = "";
    String Snippet = "";
    String Latitude = "";
    String Longitude = "";
    String name = "";
    String city_code = "";
    String state_code = "";

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSnippet() {
        return Snippet;
    }

    public void setSnippet(String snippet) {
        Snippet = snippet;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String nm) {
        name = nm;
    }

    public String getCitycode() {
        return city_code;
    }

    public void setCitycode(String cc) {
        city_code = cc;
    }

    public String getStatecode() {
        return state_code;
    }

    public void setStatecode(String sc) {
        state_code = sc;
    }
}