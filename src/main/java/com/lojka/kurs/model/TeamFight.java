package com.lojka.kurs.model;

import java.io.Serializable;

public class TeamFight implements Serializable {
    public Integer start, end, last_death,deaths;
    public PlayerInTeamFight[] players;
}
