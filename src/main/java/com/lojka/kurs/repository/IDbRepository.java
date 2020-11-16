package com.lojka.kurs.repository;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DbConnectionClosedException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.HeroRole;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.Match;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//interacting with my db
public interface IDbRepository {
    Boolean isConnectionsClosed();
    //set connection to db. Do it before other functions
    void setDbConnection(Connection c)throws DbAccessException;
    //connect heroes with their roles
    void addHeroesRolesToHeroes(Map<Integer, Hero> heroes, Map<Integer, HeroRole> roles) throws DbAccessException, DbConnectionClosedException;
    //getting data from db
    Map<Integer, Hero> getHeroes() throws DbAccessException, DbConnectionClosedException;
    Map<Integer, Item> getItems() throws DbAccessException, DbConnectionClosedException;
    Map<Integer, HeroRole> getHeroRoles() throws DbAccessException, DbConnectionClosedException;
    //update db according to internet data
    void updateItems(Item[] items) throws DbAccessException, DbConnectionClosedException;
    void updateHeroRoles(HeroRole[] roles) throws DbAccessException, DbConnectionClosedException;
    void updateHeroes(Hero[] heroes) throws DbAccessException, DbConnectionClosedException;
    void insertMatch(Match match) throws DbAccessException, DbConnectionClosedException;
    Long getLowestMatchId() throws DbAccessException, DbConnectionClosedException;
    void clearTables()throws DbAccessException, DbConnectionClosedException;
    ResultSet executeQuery(String sql) throws DbAccessException, DbConnectionClosedException;
}
