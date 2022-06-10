package com.msimplelogic.model;

import java.io.Serializable;

public class New_Model implements Serializable {
    String code;
    String name;
    String client_project_id;
    String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getClient_project_id() {
        return client_project_id;
    }

    public void setClient_project_id(String client_project_id) {
        this.client_project_id = client_project_id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
