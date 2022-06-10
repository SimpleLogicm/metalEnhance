package com.msimplelogic.model;

public class UserObject {

    private String imei;
    private String username;
    private String password;
    private String address;
    private String phone;
    private String email;
    private boolean loginOption;

//    public UserObject(String username, String email, String password, String address, String phone, boolean loginOption) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//        this.address = address;
//        this.phone = phone;
//        this.loginOption = loginOption;
//    }

    public UserObject(String imei, String username, String password) {
        this.imei = imei;
        this.username = username;
        this.password = password;
//        this.email = email;
//        this.address = address;
//        this.phone = phone;
//        this.loginOption = loginOption;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isLoginOption() {
        return loginOption;
    }
}
