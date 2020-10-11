package com.lojka.kurs.repository.oracle;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.repository.IDbConnection;

import java.sql.Connection;
import java.util.List;

public class OracleDbConnection implements IDbConnection {
    Connection connection;
    @Override
    public void setDbConnection(Connection c) {
        connection = c;
    }

    @Override
    public void insertHeroesData(List<Hero> heroes) {

    }

    @Override
    public List<Hero> getHeroes() {
        return null;
    }
}
