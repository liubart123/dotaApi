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
    public Float teamfight_participation;
    public Integer last_hits, denies;

    public Integer hero_damage, hero_healing,tower_damage;
    public Integer level, observer_uses, sentry_uses;
    public Integer lane_efficiency_pct;
    public Float stuns;

    public BoughtItem[] purchase_log;

    public Integer getLast_hits() {
        return last_hits;
    }

    public void setLast_hits(Integer last_hits) {
        this.last_hits = last_hits;
    }

    public Integer getDenies() {
        return denies;
    }

    public void setDenies(Integer denies) {
        this.denies = denies;
    }

    public Float getTeamfight_participation() {
        return teamfight_participation;
    }

    public void setTeamfight_participation(Float teamfight_participation) {
        this.teamfight_participation = teamfight_participation;
    }

    public Integer getHero_id() {
        return hero_id;
    }

    public void setHero_id(Integer hero_id) {
        this.hero_id = hero_id;
    }

    public Long getMatch_id() {
        return match_id;
    }

    public void setMatch_id(Long match_id) {
        this.match_id = match_id;
    }

    public Long getAccount_id() {
        return account_id;
    }

    public void setAccount_id(Long account_id) {
        this.account_id = account_id;
    }

    public Integer getAssists() {
        return assists;
    }

    public void setAssists(Integer assists) {
        this.assists = assists;
    }

    public Integer getDeaths() {
        return deaths;
    }

    public void setDeaths(Integer deaths) {
        this.deaths = deaths;
    }

    public Integer getKills() {
        return kills;
    }

    public void setKills(Integer kills) {
        this.kills = kills;
    }

    public Integer getTowers_killed() {
        return towers_killed;
    }

    public void setTowers_killed(Integer towers_killed) {
        this.towers_killed = towers_killed;
    }

    public Integer getCourier_kills() {
        return courier_kills;
    }

    public void setCourier_kills(Integer courier_kills) {
        this.courier_kills = courier_kills;
    }

    public Integer getObserver_kills() {
        return observer_kills;
    }

    public void setObserver_kills(Integer observer_kills) {
        this.observer_kills = observer_kills;
    }

    public Integer getSentry_kills() {
        return sentry_kills;
    }

    public void setSentry_kills(Integer sentry_kills) {
        this.sentry_kills = sentry_kills;
    }

    public Integer getLane_role() {
        return lane_role;
    }

    public void setLane_role(Integer lane_role) {
        this.lane_role = lane_role;
    }

    public Byte getCamps_stacked() {
        return camps_stacked;
    }

    public void setCamps_stacked(Byte camps_stacked) {
        this.camps_stacked = camps_stacked;
    }

    public Integer getGold_spent() {
        return gold_spent;
    }

    public void setGold_spent(Integer gold_spent) {
        this.gold_spent = gold_spent;
    }

    public Integer[] getGold_t() {
        return gold_t;
    }

    public void setGold_t(Integer[] gold_t) {
        this.gold_t = gold_t;
    }

    public Integer[] getLh_t() {
        return lh_t;
    }

    public void setLh_t(Integer[] lh_t) {
        this.lh_t = lh_t;
    }

    public Integer[] getXp_t() {
        return xp_t;
    }

    public void setXp_t(Integer[] xp_t) {
        this.xp_t = xp_t;
    }

    public Integer getHero_damage() {
        return hero_damage;
    }

    public void setHero_damage(Integer hero_damage) {
        this.hero_damage = hero_damage;
    }

    public Integer getHero_healing() {
        return hero_healing;
    }

    public void setHero_healing(Integer hero_healing) {
        this.hero_healing = hero_healing;
    }

    public Integer getTower_damage() {
        return tower_damage;
    }

    public void setTower_damage(Integer tower_damage) {
        this.tower_damage = tower_damage;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getObserver_uses() {
        return observer_uses;
    }

    public void setObserver_uses(Integer observer_uses) {
        this.observer_uses = observer_uses;
    }

    public Integer getSentry_uses() {
        return sentry_uses;
    }

    public void setSentry_uses(Integer sentry_uses) {
        this.sentry_uses = sentry_uses;
    }

    public Integer getLane_efficiency_pct() {
        return lane_efficiency_pct;
    }

    public void setLane_efficiency_pct(Integer lane_efficiency_pct) {
        this.lane_efficiency_pct = lane_efficiency_pct;
    }

    public Float getStuns() {
        return stuns;
    }

    public void setStuns(Float stuns) {
        this.stuns = stuns;
    }

    public BoughtItem[] getPurchase_log() {
        return purchase_log;
    }

    public void setPurchase_log(BoughtItem[] purchase_log) {
        this.purchase_log = purchase_log;
    }
    //public Float teamfight_participation; //0-1
    //public String personaname, name;
    //public Boolean is_roaming;
}
