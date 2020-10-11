package com.lojka.kurs.repository;

import com.lojka.kurs.model.Hero;

import java.sql.Connection;
import java.util.List;

public interface IDbConnection {
    void setDbConnection(Connection c);
    void insertHeroesData(List<Hero> heroes);
    List<Hero> getHeroes();
}
