package com.lojka.kurs.model;

import java.io.Serializable;
import java.util.Arrays;

public class Match implements Serializable {
    public Long match_id;
    public Player[] players;
    public Boolean barracks_status_dire;
    public Byte barracks_status_radiant;
    public Integer dire_score;
    public Integer radiant_score;
    public Long dire_team_id;
    public Long radiant_team_id;
    public DraftedItem[] draft_timings;
    public Integer duration;
    public Integer engine;
    public Integer first_blood_time;
    public Byte game_mode;
    public Byte human_players;
    public Integer leagueid;
    public Byte lobby_type;
    public Long match_seq_num;
    public Short negative_votes;
    public Short positive_votes;
    public Integer[] radiant_gold_adv;
    public Integer[] radiant_xp_adv;
    public Boolean radiant_win;
    public Byte skill;
    public Long start_time;
    public Short tower_status_dire;
    public Short tower_status_radiant;
    public Short version;
    public Long replay_salt, series_id;
    public Byte series_type;
    public League league;
    public Team radiant_team, dire_team;
    public Integer patch;
    public String replay_url;

    public Pick_Ban[] pick_bans;
    public TeamFight[] teamFights;
}
