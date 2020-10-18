package com.lojka.kurs.model;

import java.io.Serializable;
import java.util.Date;

public class Match implements Serializable {
    Long match_id;//
    PlayerInMatch[] players;//
    Integer dire_score;//
    Integer radiant_score;//
    Integer duration;//
    //public Integer[] radiant_gold_adv;
    //public Integer[] radiant_xp_adv;
    Boolean radiant_win;
    Integer skill;//
    Short patch;//
    Long start_time;
    Date start_date;
    Object picks_bans;

    public Object getPicks_bans() {
        return picks_bans;
    }

    public void setPicks_bans(Object picks_bans) {
        this.picks_bans = picks_bans;
    }

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
        start_date = new Date(start_time*1000);
    }

    public Long getMatch_id() {
        return match_id;
    }

    public void setMatch_id(Long match_id) {
        this.match_id = match_id;
    }

    public PlayerInMatch[] getPlayers() {
        return players;
    }

    public void setPlayers(PlayerInMatch[] players) {
        this.players = players;
    }

    public Integer getDire_score() {
        return dire_score;
    }

    public void setDire_score(Integer dire_score) {
        this.dire_score = dire_score;
    }

    public Integer getRadiant_score() {
        return radiant_score;
    }

    public void setRadiant_score(Integer radiant_score) {
        this.radiant_score = radiant_score;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getRadiant_win() {
        return radiant_win;
    }

    public void setRadiant_win(Boolean radiant_win) {
        this.radiant_win = radiant_win;
    }

    public Integer getSkill() {
        if (skill==null){
            //return -1;//;)
        }
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }

    public Short getPatch() {
        return patch;
    }

    public void setPatch(Short patch) {
        this.patch = patch;
    }
}
