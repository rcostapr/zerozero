package com.myfeup.zerozero;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Competition implements Serializable {
    private int id;
    private String name;
    private String image;
    private int sportid;
    private int ongoing;
    private String normalcolor;
    private String darkcolor;
    private Date listDate = new Date();
    private String absImgFileName;

    public String getAbsImgFileName() {
        return absImgFileName;
    }

    public void setAbsImgFileName(String absImgFileName) {
        this.absImgFileName = absImgFileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSportid(int sportid) {
        this.sportid = sportid;
    }

    public void setOngoing(int ongoing) {
        this.ongoing = ongoing;
    }

    public void setNormalcolor(String normalcolor) {
        this.normalcolor = normalcolor;
    }

    public void setDarkcolor(String darkcolor) {
        this.darkcolor = darkcolor;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getSportid() {
        return sportid;
    }

    public int getOngoing() {
        return ongoing;
    }

    public String getNormalcolor() {
        return normalcolor;
    }

    public String getDarkcolor() {
        return darkcolor;
    }

    public Competition(int ID, String NAME, String IMAGE, int SPORTID, int ONGOING, String NORMALCOLOR, String DARKCOLOR){
        this.id=ID;
        this.name=NAME;
        this.image=IMAGE;
        this.sportid=SPORTID;
        this.ongoing=ONGOING;
        this.normalcolor=NORMALCOLOR;
        this.darkcolor=DARKCOLOR;
    }

    public long getDiffMinutes(Date compareDate){
        long diffInMillies = Math.abs(compareDate.getTime() - listDate.getTime());
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }
}
