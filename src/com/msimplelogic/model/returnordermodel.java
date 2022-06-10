package com.msimplelogic.model;

public class returnordermodel {
    String rp,mrp,name,code;

    public returnordermodel(String rp, String mrp, String name,String code) {
        this.rp = rp;
        this.mrp = mrp;
        this.name = name;
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRp() {
        return rp;
    }

    public void setRp(String rp) {
        this.rp = rp;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
