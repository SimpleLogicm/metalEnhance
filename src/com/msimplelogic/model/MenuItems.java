package com.msimplelogic.model;

/**
 * Created by sujit on 12/27/2017.
 */

public class MenuItems {

    private String name;
    private int photo;

    public MenuItems(String name, int photo) {
        this.name = name;
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }
}
