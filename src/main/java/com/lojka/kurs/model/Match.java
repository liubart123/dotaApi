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
}
