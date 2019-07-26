package com.myfeup.zerozero;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

public class State implements Serializable {

    private int state;
    private boolean internetStatus;
    private boolean WiFi;
    private String idioma = "default";
    private String idiomaCountry = "default";
    private ArrayList<TvChannel> arrayListChannel = null;
    private ArrayList<Competition> arrayListSports = null;
    private ArrayList<TvChannelList> arrayTvChannelList = new ArrayList<>();
    private ArrayList<TvSportList> arrayTvSportList = new ArrayList<>();
    private ArrayList<TvTeamList> arrayTvTeamList = new ArrayList<>();

    public State(int state){
        this.state =state;
    }

    public String getIdiomaCountry() {
        return idiomaCountry;
    }

    public void setIdiomaCountry(String idiomaCountry) {
        this.idiomaCountry = idiomaCountry;
    }

    public boolean isInternetStatus() {
        return internetStatus;
    }

    public boolean isWiFi() {
        return WiFi;
    }

    public ArrayList<TvTeamList> getArrayTvTeamList() {
        return arrayTvTeamList;
    }

    public void setArrayTvTeamList(ArrayList<TvTeamList> arrayTvTeamList) {
        this.arrayTvTeamList = arrayTvTeamList;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public ArrayList<TvSportList> getArrayTvSportList() {
        return arrayTvSportList;
    }

    public void setArrayTvSportList(ArrayList<TvSportList> arrayTvSportList) {
        this.arrayTvSportList = arrayTvSportList;
    }

    public void addArrayTvSportList(TvSportList tvSportList) {
        boolean found = false;
        if(arrayTvSportList!=null) {
            for (int k = 0; k < this.arrayTvSportList.size(); k++) {
                if (arrayTvSportList.get(k).getId() == tvSportList.getId()) {
                    arrayTvSportList.get(k).setArrayListChannel(tvSportList.getArrayListChannel());
                    found = true;
                    break;
                }
            }
        }
        if(!found){
            arrayTvSportList.add(tvSportList);
        }
        Log.d("ArrayTvSportList","SIZE -> " + arrayTvSportList.size());
    }

    public ArrayList<TvChannelList> getArrayTvChannelList() {
        return arrayTvChannelList;
    }

    public void setArrayTvChannelList(ArrayList<TvChannelList> arrayTvChannelList) {
        this.arrayTvChannelList = arrayTvChannelList;
    }

    public void addArrayTvChannelList(TvChannelList tvChannelList) {
        boolean found = false;
        if(arrayTvChannelList!=null) {
            for (int k = 0; k < this.arrayTvChannelList.size(); k++) {
                if (arrayTvChannelList.get(k).getId() == tvChannelList.getId()) {
                    arrayTvChannelList.get(k).setArrayListChannel(tvChannelList.getArrayListChannel());
                    found = true;
                    break;
                }
            }
        }
        if(!found){
            arrayTvChannelList.add(tvChannelList);
        }
        Log.d("ArrayTvChannelList","SIZE -> " + arrayTvChannelList.size());
    }

    public void addArrayTvTeamList(TvTeamList tvTeamList) {
        boolean found = false;
        if(arrayTvTeamList!=null) {
            for (int k = 0; k < this.arrayTvTeamList.size(); k++) {
                if (arrayTvTeamList.get(k).getId() == tvTeamList.getId()) {
                    arrayTvTeamList.get(k).setArrayListChannel(tvTeamList.getArrayListChannel());
                    found = true;
                    break;
                }
            }
        }
        if(!found){
            arrayTvTeamList.add(tvTeamList);
        }
        Log.d("ArrayTvTeamList","SIZE -> " + arrayTvTeamList.size());
    }

    public ArrayList<TvChannel> getArrayListChannel() {
        return arrayListChannel;
    }

    public void setArrayListChannel(ArrayList<TvChannel> arrayListChannel) {
        this.arrayListChannel = arrayListChannel;
    }

    public ArrayList<Competition> getArrayListSports() {
        return arrayListSports;
    }

    public void setArrayListSports(ArrayList<Competition> arrayListSports) {
        this.arrayListSports = arrayListSports;
    }

    public void setWiFi(boolean wiFi) {
        WiFi = wiFi;
    }

    public void setInternetStatus(boolean internetStatus) {
        this.internetStatus = internetStatus;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
