package com.lojka.kurs.repository.oracle;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.HeroRole;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.repository.IDbRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class OracleDbRepository implements IDbRepository {
    static String sqlInsertItems =  "begin INSERT_ITEM(?, ?, ?); end;";
    static String sqlInsertHeroRoles =  "begin INSERT_HERO_ROLE(?, ?); end;";
    static String sqlInsertHeroes =  "begin INSERT_HERO(?, ?); end;";
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
                if (i==122){
                    i=122;
                }
                PreparedStatement ps = connection.prepareStatement(sqlInsertItems);
                ps.setInt(1,items[i].getId());
                if (items[i].getName()==null || items[i].getName()==""){
                    ps.setString(2,items[i].getKeyName());
                }else {
                    ps.setString(2,items[i].getName());
                }
                ps.setString(3,items[i].getDescription());
                ps.execute();
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateHeroRoles(HeroRole[] roles) {
        Integer i =0;
        try {
            for (i=0;i<roles.length;i++){
                PreparedStatement ps = connection.prepareStatement(sqlInsertHeroRoles);
                ps.setInt(1,roles[i].getId());
                ps.setString(2,roles[i].getRoleName());
                ps.execute();
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateHeroes(Hero[] heroes) {
        Integer i =0;
        try {
            for (i=0;i<heroes.length;i++){
                if (heroes[i]==null)
                    continue;
                PreparedStatement ps = connection.prepareStatement(sqlInsertHeroes);
                ps.setInt(1, heroes[i].getId());
                ps.setString(2,heroes[i].getName());
                ps.execute();
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
