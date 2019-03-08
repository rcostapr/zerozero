package com.myfeup.zerozero;

import android.net.Uri;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TvChannel implements Serializable {

    private int Id;
    private String name;
    private String imgPath;
    private int imgWidth;
    private int imgHeight;
    private String domain;
    private String absImgFileName;
    private Date listDate = new Date();

    public String getAbsImgFileName() {
        return absImgFileName;
    }

    public void setAbsImgFileName(String absImgFileName) {
        this.absImgFileName = absImgFileName;
    }

    public TvChannel(int Id, String name, String imgPath, int imgWidth, int imgHeight, String domain){
        this.Id=Id;
        this.name=name;
        this.imgPath=imgPath;
        this.imgWidth=imgWidth;
        this.imgHeight=imgHeight;
        this.domain=domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public long getDiffMinutes(Date compareDate){
        long diffInMillies = Math.abs(compareDate.getTime() - listDate.getTime());
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }
}
