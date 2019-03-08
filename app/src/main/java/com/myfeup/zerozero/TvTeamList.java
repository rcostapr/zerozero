package com.myfeup.zerozero;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TvTeamList implements Serializable {

    private int Id;
    private ArrayList<Match> arrayListChannel;
    private Date listDate = new Date();

    public TvTeamList(int Id, ArrayList<Match> arrayListChannel){
        this.Id = Id;
        this.arrayListChannel = arrayListChannel;
    }

    public int getId() {
        return Id;
    }

    public ArrayList<Match> getArrayListChannel() {
        return arrayListChannel;
    }

    public void setArrayListChannel(ArrayList<Match> arrayListChannel) {
        this.arrayListChannel = arrayListChannel;
        Date now = new Date();
        this.listDate = now;
    }
    public long getDiffMinutes(Date compareDate){
        long diffInMillies = Math.abs(compareDate.getTime() - listDate.getTime());
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        return diff;
    }
}
