package com.myfeup.zerozero;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Sport implements Serializable {
    private int Id;
    private String name;
    private String shortname;
    private Date listDate = new Date();

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
    public long getDiffMinutes(Date compareDate){
        long diffInMillies = Math.abs(compareDate.getTime() - listDate.getTime());
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }
}
