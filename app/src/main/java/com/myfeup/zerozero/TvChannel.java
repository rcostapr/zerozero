package com.myfeup.zerozero;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TvChannel implements Serializable {

    private int Id;
    private String name;
    private String image;
    private String type;
    private String url;
    private String absImgFileName;
    private Date listDate = new Date();

    public String getAbsImgFileName() {
        return absImgFileName;
    }

    public void setAbsImgFileName(String absImgFileName) {
        this.absImgFileName = absImgFileName;
    }

    public TvChannel(int ID, String NAME, String IMAGE, String TYPE, String URL){
        this.Id=ID;
        this.name=NAME;
        this.image=IMAGE;
        this.type=TYPE;
        this.url=URL;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getListDate() {
        return listDate;
    }

    public void setListDate(Date listDate) {
        this.listDate = listDate;
    }

    public long getDiffMinutes(Date compareDate){
        long diffInMillies = Math.abs(compareDate.getTime() - listDate.getTime());
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }
}
