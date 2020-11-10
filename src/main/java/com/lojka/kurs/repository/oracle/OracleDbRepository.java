package com.lojka.kurs.repository.oracle;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DbConnectionClosedException;
import com.lojka.kurs.model.*;
import com.lojka.kurs.repository.IDbRepository;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Slf4j
public class OracleDbRepository implements IDbRepository {
    static String sqlInsertItems =  "begin INSERT_ITEM(?, ?, ?, ?); end;";
    static String sqlInsertHeroRoles =  "begin INSERT_HERO_ROLE(?, ?); end;";
    static String sqlInsertHeroes =  "begin INSERT_HERO(?, ?, ?, ?); end;";
    static String sqlInsertHeroesRoles =  "begin INSERT_HEROES_ROLES(?, ?); end;";
    static String sqlInsertMatch =  "begin INSERT_MATCH(?,?,?,?,?,?,?,?); end;";
    static String sqlClearHeroRoles =  "begin CLEAR_HEROES_ROLES; end;";

    static String sqlInsertMatchPlayer =  "begin INSERT_PLAYER_IN_MATCH(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); end;";
    static String sqlInsertMatchPlayerStat =  "begin INSERT_PLAYER_MATCH_STAT(?,?,?,?); end;";
    static String sqlInsertMatchPlayerItems =  "begin INSERT_BOUGHT_ITEMS(?,?,?); end;";
    static String sqlClearMatchPlayerItems =  "begin CLEAR_BOUGHT_ITEMS(?); end;";

    static String sqlGetHeroes = "begin SELECT_HEROES(?); end;";
    static String sqlGetItems = "begin SELECT_ITEMS(?); end;";
    static String sqlGetHeroRoles = "begin SELECT_HERO_ROLES(?); end;";
    static String sqlGetHeroesRoles = "begin SELECT_HEROES_ROLES(?); end;";

    static String sqlGetLowestMatchId = "begin GET_LOWEST_MATCH_ID(?); end;";

    Connection connection;


    @Override
    public Boolean isConnectionsClosed() {
        try {
            return connection.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }

    @Override
    public void setDbConnection(Connection c) {
        connection = c;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            log.error("setting autocommit error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //set connection between heroes and their roles
    @Override
    public void addHeroesRolesToHeroes(Map<Integer, Hero> heroes, Map<Integer, HeroRole> roles) throws DbAccessException, DbConnectionClosedException {
        try {
            CallableStatement st = connection.prepareCall(sqlGetHeroesRoles);
            st.registerOutParameter(1, OracleTypes.CURSOR);
            st.executeQuery();
            ResultSet rs = st.getObject(1, ResultSet.class);
            while (rs.next()){
                heroes.get(rs.getInt(1)).getRoles().add(roles.get(rs.getInt(2)));
            }
        } catch (SQLException e) {

            try {
                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
            } catch (SQLException ex) {
                log.error("db access error");
            }
            throw new DbAccessException("error during adding heroes roles to heroes: " + e.getMessage());
        }
    }
    @Override
    public Map<Integer, Hero> getHeroes() throws DbAccessException, DbConnectionClosedException {
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
                hero.setImg(rs.getString(3));
                hero.setIcon(rs.getString(4));
                //hero.setDescription(rs.getString(3));
                result.put(hero.getId(),hero);
            }
            return result;
        } catch (SQLException e) {

            try {
                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
            } catch (SQLException ex) {
                log.error("db access error");
            }
            throw new DbAccessException("error during getting heroes: " + e.getMessage());
        }
    }

    @Override
    public Map<Integer, Item> getItems() throws DbAccessException, DbConnectionClosedException {
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
            try {
                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
            } catch (SQLException ex) {
                log.error("db access error");
            }
            throw new DbAccessException("error during getting items: " + e.getMessage());
        }
    }

    @Override
    public Map<Integer, HeroRole> getHeroRoles() throws DbAccessException, DbConnectionClosedException {
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
            try {
                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
            } catch (SQLException ex) {
                log.error("db access error");
            }
            throw new DbAccessException("error during getting hero roles: " + e.getMessage());
        }
    }

    @Override
    public void updateItems(Item[] items) throws DbAccessException, DbConnectionClosedException {
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
                ps.setString(4,items[i].getKeyName());
                ps.execute();
                ps.close();
            }
        }
        catch (SQLException e) {
            try {
                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
                connection.rollback();
            } catch (SQLException ex) {
                log.error("rollback error");
            }
            throw new DbAccessException("error during inserting items into db: " + e.getMessage());
        }finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                log.error("commit error");
            }
        }
    }
    @Override
    public void updateHeroRoles(HeroRole[] roles) throws DbAccessException, DbConnectionClosedException {
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
            try {
                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
                connection.rollback();
            } catch (SQLException ex) {
                log.error("rollback error");
            }
            throw new DbAccessException("error during inserting roles into db: " + e.getMessage());
        }finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                log.error("commit error");
            }
        }
    }
    @Override
    public void updateHeroes(Hero[] heroes) throws DbAccessException, DbConnectionClosedException {
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
                ps.setString(3,heroes[i].getImg());
                ps.setString(4,heroes[i].getIcon());
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
            try {

                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
                connection.rollback();
            } catch (SQLException ex) {
                log.error("rollback error");
            }
            throw new DbAccessException("error during inserting heroes into db: " + e.getMessage());
        }finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                log.error("commit error");
            }
        }
    }

    @Override
    public void insertMatch(Match match) throws DbAccessException, DbConnectionClosedException {
        log.debug("inserting match with id: " + match.getMatch_id());
        Integer i =0;
        try {
            //filling table matches;
            CallableStatement cs = connection.prepareCall(sqlInsertMatch);
            cs.setLong(1, match.getMatch_id());
            cs.setInt(2, match.getDuration());
            cs.setInt(3, match.getDire_score());
            cs.setInt(4, match.getRadiant_score());
            if (match.getSkill()==null){
                cs.setNull(5, Types.INTEGER );
            }else {
                cs.setInt(5, match.getSkill());
            }
            if (match.getPatch()==null){
                cs.setNull(6, Types.INTEGER );
            }else {
                cs.setInt(6, match.getPatch());
            }
            cs.setBoolean(7, match.getRadiant_win());
            cs.setDate(8, new Date(match.getStart_date().getTime()));
            cs.execute();
//            if (true)
//                throw new SQLException("asd");
            cs.close();
            for (PlayerInMatch pim :
                 match.getPlayers()) {
                cs = connection.prepareCall(sqlInsertMatchPlayer);
                cs.setLong(1, pim.getAccount_id());
                cs.setLong(2, pim.getMatch_id());
                cs.setInt(3, pim.getHero_id());
                cs.setInt(4, pim.getKills());
                cs.setInt(5, pim.getDeaths());
                cs.setInt(6, pim.getAssists());
                cs.setInt(7, pim.getHero_damage());
                cs.setInt(8, pim.getHero_healing());
                cs.setInt(9, pim.getTower_damage());
                if (pim.getTeamfight_participation()==null){
                    cs.setNull(10, OracleTypes.FLOAT);
                }else {
                    cs.setFloat(10, pim.getTeamfight_participation());
                }
                cs.registerOutParameter(11, OracleTypes.NUMBER);
                cs.setInt(12, pim.getTowers_killed());
                cs.setInt(13, pim.getCourier_kills());
                cs.setInt(14, pim.getSentry_kills());
                cs.setInt(15, pim.getSentry_uses());
                cs.setInt(16, pim.getObserver_kills());
                cs.setInt(17, pim.getObserver_uses());
                if (pim.getCamps_stacked()!=null){
                    cs.setInt(18, pim.getCamps_stacked());
                }else {
                    cs.setNull(18, OracleTypes.INTEGER);
                }
                cs.setInt(19, pim.getLast_hits());
                cs.setInt(20, pim.getDenies());
                cs.setFloat(21, pim.getStuns());
                if (pim.getLane_efficiency_pct()==null){
                    cs.setNull(22, OracleTypes.INTEGER);
                }else {
                    cs.setInt(22, pim.getLane_efficiency_pct());
                }
                cs.setBoolean(24, pim.getWin());
                cs.registerOutParameter(23, OracleTypes.INTEGER);
                cs.executeQuery();
                //BigInteger matchPlayerId = cs.getObject(11, BigInteger.class);
                Integer wasInsertedNewMatch = cs.getObject(23, Integer.class);
                BigDecimal matchPlayerIdDec = cs.getObject(11, BigDecimal.class);
                cs.close();
                if (wasInsertedNewMatch==1){
                    continue;
                }
                //inserting gpm and xpm
                for(int j=0;j<pim.getGold_t().length;j++){
                    cs = connection.prepareCall(sqlInsertMatchPlayerStat);
                    cs.setBigDecimal(1, matchPlayerIdDec);
                    cs.setInt(2, j);
                    cs.setInt(3, pim.getGold_t()[j]);
                    cs.setInt(4, pim.getXp_t()[j]);
                    cs.executeQuery();
                    cs.close();
                }
                //clear already existing items log for this pim
                cs = connection.prepareCall(sqlClearMatchPlayerItems);
                cs.setBigDecimal(1,matchPlayerIdDec);
                cs.executeQuery();
                cs.close();
                //inserting items bought log
                for(BoughtItem item : pim.getPurchase_log()){

                    cs = connection.prepareCall(sqlInsertMatchPlayerItems);
                    cs.setInt(1, item.getItemId());
                    cs.setBigDecimal(2, matchPlayerIdDec);
                    cs.setInt(3, item.getTime());
                    cs.executeQuery();
                    cs.close();
                }
            }
        } catch (SQLException e) {
            try {
                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
                connection.rollback();
            } catch (SQLException ex) {
                log.error("rollback error");
            }
            throw new DbAccessException("error during inserting match into db: " + e.getMessage());
        }catch ( Exception e){
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.error("rollback error");
            }
            throw new DbAccessException("strange error during inserting match into db: " + e.getMessage());
        }
        finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                log.error("commit error");
            }
        }
    }

    @Override
    public Long getLowestMatchId() throws DbAccessException, DbConnectionClosedException {
        log.debug("getting lowest match id");
        Long rs = -1l;
        try {
            //filling table matches;
            CallableStatement cs = connection.prepareCall(sqlGetLowestMatchId);
            cs.registerOutParameter(1, OracleTypes.NUMBER);
            cs.execute();
            rs = cs.getObject(1, Long.class);
            cs.close();
        } catch (SQLException e) {
            try {
                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
                connection.rollback();
            } catch (SQLException ex) {
                log.error("rollback error");
            }
            throw new DbAccessException("error during getting lowest match id: " + e.getMessage());
        }catch ( Exception e){
            try {
                connection.rollback();
            } catch (SQLException ex) {
                log.error("rollback error");
            }
            throw new DbAccessException("strange error during getting lowest match id: " + e.getMessage());
        }
        finally {
            try {
                connection.commit();
            } catch (SQLException e) {
                log.error("commit error");
            }
        }
        return rs;
    }

    @Override
    public ResultSet executeQuery(String sql) throws DbAccessException, DbConnectionClosedException{
        try {
            log.debug("execute custom query: " + sql);
            ResultSet rs = connection.prepareStatement(sql).executeQuery();
            return rs;
        } catch (SQLException e) {
            try {
                if (connection.isClosed()){
                    throw new DbConnectionClosedException("db connection was closed...");
                }
            } catch (SQLException ex) {
                log.error("rollback error");
            }
            throw new DbAccessException("error executing custom query: " + e.getMessage());
        }

    }

}
