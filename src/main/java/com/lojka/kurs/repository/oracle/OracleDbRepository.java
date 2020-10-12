package com.lojka.kurs.repository.oracle;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.repository.IDbRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OracleDbRepository implements IDbRepository {
    static String sqlInsertItems =  "begin INSERT_ITEM(?, ?, ?); end;";
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
    public void updateItems(Item[] items) {
        Integer i =0;
        try {
            for (i=0;i<items.length;i++){
                PreparedStatement ps = connection.prepareStatement(sqlInsertItems);
                ps.setInt(1,items[i].getId());
                if (items[i].getName()==null || items[i].getName()==""){
                    ps.setString(2,items[i].getKeyName());
                }else {
                    ps.setString(2,items[i].getName());
                }
                ps.setString(3,items[i].getDescription());
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
