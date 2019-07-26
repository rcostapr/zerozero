package com.myfeup.zerozero;

import java.io.Serializable;

public class Match implements Serializable {

    private int matchId;
    private int channelId;
    private int homeTeamId;
    private int awayTeamId;
    private int sportsId;
    private int competitionId;

    private String toolTip;
    private String matchDay;
    private String homeTeam;
    private String awayTeam;

    private String absfileImgHomeTeam;
    private String absfileImgAwayTeam;

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public int getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(int homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public int getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(int awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public int getSportsId() {
        return sportsId;
    }

    public void setSportsId(int sportsId) {
        this.sportsId = sportsId;
    }

    public int getCompetitionId() {
        return competitionId;
    }

    public void setCompetitionId(int competitionId) {
        this.competitionId = competitionId;
    }

    public String getToolTip() {
        return toolTip;
    }

    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    public String getMatchDay() {
        return matchDay;
    }

    public void setMatchDay(String matchDay) {
        this.matchDay = matchDay;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        this.awayTeam = awayTeam;
    }

    public String getAbsfileImgHomeTeam() {
        return absfileImgHomeTeam;
    }

    public void setAbsfileImgHomeTeam(String absfileImgHomeTeam) {
        this.absfileImgHomeTeam = absfileImgHomeTeam;
    }

    public String getAbsfileImgAwayTeam() {
        return absfileImgAwayTeam;
    }

    public void setAbsfileImgAwayTeam(String absfileImgAwayTeam) {
        this.absfileImgAwayTeam = absfileImgAwayTeam;
    }

    public Match(int matchId, int channelId, int competitionId, String matchDay, int homeTeamId, int awayTeamId, String homeTeam, String awayTeam, int sportsId, String toolTip) {
        this.matchDay=matchDay;
        this.matchId=matchId;
        this.channelId=channelId;
        this.homeTeam=homeTeam;
        this.homeTeamId=homeTeamId;
        this.toolTip=toolTip;
        this.awayTeam=awayTeam;
        this.awayTeamId=awayTeamId;
        this.sportsId=sportsId;
        this.competitionId=competitionId;
    }
}
