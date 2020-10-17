package com.lojka.kurs.repository;

import com.lojka.kurs.exception.DbAccessException;
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
    void setDbConnection(Connection c)throws DbAccessException;
    //connect heroes with their roles
    void addHeroesRolesToHeroes(Map<Integer, Hero> heroes, Map<Integer, HeroRole> roles) throws DbAccessException;
    //getting data from db
    Map<Integer, Hero> getHeroes()throws DbAccessException;
    Map<Integer, Item> getItems()throws DbAccessException;
    Map<Integer, HeroRole> getHeroRoles()throws DbAccessException;
    //update db according to internet data
    void updateItems(Item[] items)throws DbAccessException;
    void updateHeroRoles(HeroRole[] roles)throws DbAccessException;
    void updateHeroes(Hero[] heroes)throws DbAccessException;
    void insertMatch(Match match)throws DbAccessException;
}
