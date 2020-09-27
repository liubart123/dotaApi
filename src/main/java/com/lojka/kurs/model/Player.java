package com.lojka.kurs.model;

import java.io.Serializable;

public class Player implements Serializable {
    public Integer hero_id;
    public Byte player_slot;
    public Integer[] ability_upgrades_arr;
    public Long account_id;
    public Byte assists, deaths, kills,towers_killed,courier_kills,observer_kills,sentry_kills,lane_role;
    public Integer  gold, gold_per_min,xp_per_min, total_gold,total_xp,actions_per_mindenies,ancient_kills,last_hits;
    public Byte camps_stacked;
    public Integer gold_spent;
    public Integer[] gold_t;

    public Integer hero_damage, hero_healing,tower_damage;
    public Short item_0,item_1,item_2,item_3,item_4,item_5,item_neutral;
    public Byte level, obs_placed;
    public Integer[] lh_t,xp_t;
    public Integer stuns;
    //public Float teamfight_participation; //0-1
    public String personaname, name;
    public Boolean is_roaming;
}
