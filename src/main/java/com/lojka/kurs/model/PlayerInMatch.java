package com.lojka.kurs.model;

import java.io.Serializable;

public class PlayerInMatch implements Serializable {
    public Integer hero_id;
    public Long match_id;
    public Long account_id;
    public Integer assists, deaths, kills,towers_killed,courier_kills,observer_kills,sentry_kills,lane_role;
    //public Integer  gold, gold_per_min,xp_per_min, total_gold,total_xp,actions_per_mindenies,ancient_kills,last_hits;
    public Byte camps_stacked;
    public Integer gold_spent;
    public Integer[] gold_t;
    public Integer[] lh_t,xp_t;

    public Integer hero_damage, hero_healing,tower_damage;
    public Integer level, observer_uses, sentry_uses;
    public Integer lane_efficiency_pct;
    public Integer stuns;

    public BoughtItem[] purchase_log;
    //public Float teamfight_participation; //0-1
    //public String personaname, name;
    //public Boolean is_roaming;
}
