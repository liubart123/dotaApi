package com.lojka.kurs.model;

import java.io.Serializable;

public class Match implements Serializable {
    public Long match_id;//
    public PlayerInMatch[] players;//
    public Integer dire_score;//
    public Integer radiant_score;//
    public Integer duration;//
    //public Integer[] radiant_gold_adv;
    //public Integer[] radiant_xp_adv;
    public Boolean radiant_win;
    public Integer skill;//
    public Short version;//

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

    public Short getVersion() {
        return version;
    }

    public void setVersion(Short version) {
        this.version = version;
    }
}
