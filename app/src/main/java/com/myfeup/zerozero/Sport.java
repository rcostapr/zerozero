package com.myfeup.zerozero;

import java.io.Serializable;

public class Sport implements Serializable {
    private int Id;
    private String name;
    private String shortname;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Sport(int Id, String name, String shortname) {
        this.Id = Id;
        this.name = name;
        this.shortname=shortname;
    }
}
