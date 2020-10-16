package com.lojka.kurs.repository.oracle;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.HeroRole;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.Match;
import com.lojka.kurs.repository.IDbRepository;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class OracleDbRepository implements IDbRepository {
    static String sqlInsertItems =  "begin INSERT_ITEM(?, ?, ?, ?); end;";
    static String sqlInsertHeroRoles =  "begin INSERT_HERO_ROLE(?, ?); end;";
    static String sqlInsertHeroes =  "begin INSERT_HERO(?, ?); end;";
    static String sqlInsertHeroesRoles =  "begin INSERT_HEROES_ROLES(?, ?); end;";
    static String sqlInsertMatch =  "begin INSERT_MATCH(?,?,?,?,?,?,?); end;";
    static String sqlClearHeroRoles =  "begin CLEAR_HEROES_ROLES; end;";

    static String sqlGetHeroes = "begin SELECT_HEROES(?); end;";
    static String sqlGetItems = "begin SELECT_ITEMS(?); end;";
    static String sqlGetHeroRoles = "begin SELECT_HERO_ROLES(?); end;";
    static String sqlGetHeroesRoles = "begin SELECT_HEROES_ROLES(?); end;";

    Connection connection;
    @Override
    public void setDbConnection(Connection c) {
        connection = c;
    }

    //set connection between heroes and their roles
    @Override
    public void addHeroesRolesToHeroes(Map<Integer, Hero> heroes, Map<Integer, HeroRole> roles) {
        try {
            CallableStatement st = connection.prepareCall(sqlGetHeroesRoles);
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.executeQuery();
            ResultSet rs = st.getObject(1, ResultSet.class);
            while (rs.next()){
                heroes.get(rs.getInt(1)).getRoles().add(roles.get(rs.getInt(2)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Map<Integer, Hero> getHeroes() {
        try {
            Map<Integer, Hero> result = new HashMap<>();
            CallableStatement st = connection.prepareCall(sqlGetHeroes);
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.executeQuery();
            ResultSet rs = st.getObject(1, ResultSet.class);
            while (rs.next()){
                Hero hero = new Hero();
                hero.setId(rs.getInt(1));
                hero.setName(rs.getString(2));
                //hero.setDescription(rs.getString(3));
                result.put(hero.getId(),hero);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<Integer, Item> getItems() {
        try {
            Map<Integer, Item> result = new HashMap<>();
            CallableStatement st = connection.prepareCall(sqlGetItems);
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.executeQuery();
            ResultSet rs = st.getObject(1, ResultSet.class);
            while (rs.next()){
                Item tempItem = new Item();
                tempItem.setId(rs.getInt(1));
                tempItem.setName(rs.getString(2));
                tempItem.setDescription(rs.getString(3));
                tempItem.setKeyName(rs.getString(4));
                result.put(tempItem.getId(),tempItem);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<Integer, HeroRole> getHeroRoles() {
        try {
            Map<Integer, HeroRole> result = new HashMap<>();
            CallableStatement st = connection.prepareCall(sqlGetHeroRoles);
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.executeQuery();
            ResultSet rs = st.getObject(1, ResultSet.class);
            while (rs.next()){
                HeroRole heroRole = new HeroRole();
                heroRole.setId(rs.getInt(2));
                heroRole.setRoleName(rs.getString(1));
                result.put(heroRole.getId(),heroRole);
            }
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                ps.setString(4,items[i].getKeyName());
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
            //filling table hero_roles
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
            //clear talbe heroes_roles
            PreparedStatement ps = connection.prepareStatement(sqlClearHeroRoles);
            ps.execute();
            ps.close();
            //filling table heroes;
            for (i=0;i<heroes.length;i++){
                if (heroes[i]==null)
                    continue;
                ps = connection.prepareStatement(sqlInsertHeroes);
                ps.setInt(1, heroes[i].getId());
                ps.setString(2,heroes[i].getName());
                ps.execute();
                ps.close();
                for (HeroRole role : heroes[i].getRoles())
                {
                    ps = connection.prepareStatement(sqlInsertHeroesRoles);
                    ps.setInt(1, heroes[i].getId());
                    ps.setInt(2, role.getId());
                    ps.execute();
                    ps.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertMatch(Match match) {
        Integer i =0;
        try {
            //filling table matches;
            PreparedStatement ps = connection.prepareStatement(sqlInsertMatch);
            ps.setLong(1, match.getMatch_id());
            ps.setInt(2, match.getDuration());
            ps.setInt(3, match.getDire_score());
            ps.setInt(4, match.getRadiant_score());
            if (match.getSkill()==null){
                ps.setNull(5, Types.INTEGER );
            }else {
                ps.setInt(5, match.getSkill());
            }
            ps.setInt(6, match.getVersion());
            ps.setBoolean(7, match.getRadiant_win());
            ps.execute();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
