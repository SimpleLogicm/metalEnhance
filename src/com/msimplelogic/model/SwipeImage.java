package com.msimplelogic.model;

import java.io.Serializable;

public class SwipeImage implements Serializable {

    private String image;

    public SwipeImage() {

    }

    public SwipeImage(String image) {
        this.image = image;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
