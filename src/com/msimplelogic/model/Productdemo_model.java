package com.msimplelogic.model;

public class Productdemo_model {
    public String image;
    public String Productname;
    public String proddiscrip;
    public String lastsync;

    public Productdemo_model(String image, String productname, String proddiscrip, String lastsync) {
        this.image = image;
        Productname = productname;
        this.proddiscrip = proddiscrip;
        this.lastsync = lastsync;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
    }

    public String getLastsync() {
        return lastsync;
    }

    public void setLastsync(String lastsync) {
        this.lastsync = lastsync;
    }

    public String getProddiscrip() {
        return proddiscrip;
    }

    public void setProddiscrip(String proddiscrip) {
        this.proddiscrip = proddiscrip;
    }
}
