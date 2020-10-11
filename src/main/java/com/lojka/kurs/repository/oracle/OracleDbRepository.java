package com.lojka.kurs.repository.oracle;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.repository.IDbRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class OracleDbRepository implements IDbRepository {
    Connection connection;
    @Override
    public void setDbConnection(Connection c) {
        connection = c;
    }

    @Override
    public void insertHeroesData(Hero[] heroes) {

    }
    @Override
    public List<Hero> getHeroes() {
        return null;
    }

    @Override
    public void insertItems(Item[] items) {
        try {
            PreparedStatement ps = connection.prepareStatement("begin\n" +
                    "  INSERT_ITEM(12346, 'hero2', 'desc2');\n" +
                    "end;");
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
