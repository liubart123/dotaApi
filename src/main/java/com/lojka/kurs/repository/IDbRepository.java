package com.lojka.kurs.repository;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;

import java.sql.Connection;
import java.util.List;

public interface IDbRepository {
    void setDbConnection(Connection c);
    void insertHeroesData(Hero[] heroes);
    List<Hero> getHeroes();
    void insertItems(Item[] items);
}
