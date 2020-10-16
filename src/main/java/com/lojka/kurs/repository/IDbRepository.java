package com.lojka.kurs.repository;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.HeroRole;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.Match;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface IDbRepository {
    //set connection to db. Do it before other functions
    void setDbConnection(Connection c);
    //connect heroes with their roles
    void addHeroesRolesToHeroes(Map<Integer, Hero> heroes, Map<Integer, HeroRole> roles);
    //getting data from db
    Map<Integer, Hero> getHeroes();
    Map<Integer, Item> getItems();
    Map<Integer, HeroRole> getHeroRoles();
    //update db according to internet data
    void updateItems(Item[] items);
    void updateHeroRoles(HeroRole[] roles);
    void updateHeroes(Hero[] heroes);
    void insertMatch(Match match);
}
