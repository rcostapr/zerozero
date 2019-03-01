package com.myfeup.zerozero;

class Match {

    private int matchId;
    private int channelId;
    private int homeTeamId;
    private int awayTeamId;
    private int sportsId;

    private String domain;
    private String matchDay;
    private String homeTeam;
    private String awayTeam;
    private String channel;
    private String sports;

    private String absfileImgHomeTeam;
    private String absfileImgAwayTeam;

    public Match(int matchId, int channelId, String domain, String matchDay, int homeTeamId, int awayTeamId, String homeTeam, String awayTeam, int sportsId, String channel, String sports) {
        this.matchDay=matchDay;
        this.matchId=matchId;
        this.channelId=channelId;
        this.homeTeam=homeTeam;
        this.homeTeamId=homeTeamId;
        this.domain=domain;
        this.awayTeam=awayTeam;
        this.awayTeamId=awayTeamId;
        this.channel=channel;
        this.sports=sports;
        this.sportsId=sportsId;
    }

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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
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

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getSports() {
        return sports;
    }

    public void setSports(String sports) {
        this.sports = sports;
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

}
