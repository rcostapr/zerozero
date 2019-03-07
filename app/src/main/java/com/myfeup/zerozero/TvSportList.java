package com.myfeup.zerozero;

import java.io.Serializable;
import java.util.ArrayList;

public class TvSportList implements Serializable {

    int Id;
    ArrayList<Match> arrayListChannel;

    public TvSportList(int Id, ArrayList<Match> arrayListChannel){
        this.Id = Id;
        this.arrayListChannel = arrayListChannel;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public ArrayList<Match> getArrayListChannel() {
        return arrayListChannel;
    }

    public void setArrayListChannel(ArrayList<Match> arrayListChannel) {
        this.arrayListChannel = arrayListChannel;
    }
}
