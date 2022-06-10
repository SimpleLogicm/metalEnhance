package com.msimplelogic.model;

public class Ratings_model {
    Float no_stars;
    String title;
    Double total;
    String date;

    public Float getNo_stars() {
        return no_stars;
    }

    public void setNo_stars(Float no_stars) {
        this.no_stars = no_stars;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Ratings_model(Float no_stars, String title, Double total, String date) {
        this.no_stars = no_stars;
        this.title = title;
        this.total = total;
        this.date = date;
    }
}
