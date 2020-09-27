package com.lojka.kurs.model;

import java.io.Serializable;

public class PlayerInTeamFight implements Serializable {
    public Byte deaths, buybacks;
    public Integer damage, healing, gold_delta, xp_delta, xp_start;
    public String hero_name;
}
