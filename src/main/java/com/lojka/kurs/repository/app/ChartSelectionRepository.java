package com.lojka.kurs.repository.app;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.queriesV2.*;
import com.lojka.kurs.model.user.User;
import com.lojka.kurs.service.super_service.SuperService;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.OracleTypes;
import org.springframework.stereotype.Repository;

import java.nio.file.AccessDeniedException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Repository
public class ChartSelectionRepository {

    static String hostName = "192.168.0.106";
    static String sid = "/pdb_kurs";
    static String userName = "pdb_kurs_app_admin";
    static String password = "password";
    Connection connection;

    String sqlInsertSelection = "begin insert_selection(?,?,?,?,?,?,?,?,?,?); end;";
    String sqlInsertSelectionHeroes = "begin insert_selection_heroes(?,?,?); end;";
    String sqlClearSelectionHeroes = "begin clear_selection_heroes(?); end;";
    String sqlInsertSelectionItems = "begin insert_selection_items(?,?,?); end;";
    String sqlClearSelectionItems = "begin clear_selection_items(?); end;";

    String sqlSelectSelections = "begin SELECT_selections(?,?); end;";
    String sqlSelectSelection = "begin get_selection(?,?); end;";
    String sqlSelectSelectionsHeroes = "begin SELECT_selections_heroes(?,?); end;";
    String sqlSelectSelectionsItems = "begin SELECT_selections_items(?,?); end;";

    public Connection getConnection() throws SQLException, DbAccessException {
        if (connection==null || connection.isClosed()){
            try {
                Class.forName("oracle.jdbc.driver.OracleDriver");
                String connectionURL = "jdbc:oracle:thin:@" + hostName + ":1521" + sid;

                connection = DriverManager.getConnection(connectionURL, userName,
                        password);
                return connection;
            } catch (ClassNotFoundException e) {
                throw new DbAccessException("drive class not found");
            } catch (SQLException e){
                throw new DbAccessException("error with connection to db");
            }
        }
        return connection;
    }

    public void insertSelection(Selection selection, User user)throws SQLException, DbAccessException{
        log.debug("insert selection by " + user.getLogin());
        CallableStatement cs = getConnection().prepareCall(sqlInsertSelection);
        cs.setString(1, selection.getSelectionName());
        cs.setInt(2, selection.getDurationMin());
        cs.setInt(3, selection.getDurationMax());
        cs.setInt(4, selection.getPatchMin());
        cs.setInt(5, selection.getPatchMax());
        cs.setDate(6, new Date(selection.getDateMin().getTime()));
        cs.setDate(7, new Date(selection.getDateMax().getTime()));
        cs.setInt(8, selection.getHero().getId());
        cs.setInt(9, user.getId());
        cs.registerOutParameter(10, OracleTypes.INTEGER);
        if (cs.executeQuery()!=null){
            if (cs.getInt(10)<0){
                throw new DbAccessException("Selection with this name is already exist");
            }
            selection.setId(cs.getInt(10));
        }else {
            throw new DbAccessException("error with query");
        }

    }
    public void insertSelectionsHeroesAndItems(Selection selection, User user)throws SQLException, DbAccessException{
        log.debug("insert selection by " + user.getLogin());
        CallableStatement cs = getConnection().prepareCall(sqlClearSelectionHeroes);
        cs.setInt(1, selection.getId());
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            throw new DbAccessException("error with query");
        }
        log.debug("inserting allies...");
        for(Hero hero : selection.getAllies()){
            cs = getConnection().prepareCall(sqlInsertSelectionHeroes);
            cs.setInt(1,selection.getId());
            cs.setInt(2,hero.getId());
            cs.setString(3,"ally");
            if (cs.executeQuery()!=null){
                cs.close();
            }else {
                throw new DbAccessException("error with query");
            }
        }
        log.debug("inserting enemies...");
        for(Hero hero : selection.getEnemies()){
            cs = getConnection().prepareCall(sqlInsertSelectionHeroes);
            cs.setInt(1,selection.getId());
            cs.setInt(2,hero.getId());
            cs.setString(3,"enemy");
            if (cs.executeQuery()!=null){
                cs.close();
            }else {
                throw new DbAccessException("error with query");
            }
        }

        log.debug("clear items");
        cs = getConnection().prepareCall(sqlClearSelectionItems);
        cs.setInt(1, selection.getId());
        if (cs.executeQuery()!=null){
            cs.close();
        }else {
            throw new DbAccessException("error with query");
        }
        log.debug("inserting items...");
        for(Item item : selection.getItems()){
            cs = getConnection().prepareCall(sqlInsertSelectionItems);
            cs.setInt(1,selection.getId());
            cs.setInt(2,item.getId());
            cs.setString(3,"boughtItem");
            if (cs.executeQuery()!=null){
                cs.close();
            }else {
                throw new DbAccessException("error with query");
            }
        }
    }

    public ArrayList<Selection> getSelections(User user)throws SQLException, DbAccessException{
        Map<Integer, Selection> selections = new HashMap<>();

        CallableStatement cs = getConnection().prepareCall(sqlSelectSelections);
        cs.setInt(2,user.getId());
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1,ResultSet.class);
        while(rs.next()){
            Selection selection = new Selection();
            selection.setId(rs.getInt("id"));
            selection.setSelectionName(rs.getString("name"));
            selection.setDurationMin(rs.getInt(3));
            selection.setDurationMax(rs.getInt(4));
            selection.setPatchMin(rs.getInt(5));
            selection.setPatchMax(rs.getInt(6));
            selection.setDateMin(rs.getDate(7));
            selection.setDateMax(rs.getDate(8));
            selection.setHero(SuperService.getHeroes().get(rs.getInt(9)));


            CallableStatement cs2 =getConnection().prepareCall(sqlSelectSelectionsHeroes);
            cs2.registerOutParameter(1,OracleTypes.CURSOR);
            cs2.setInt(2,selection.getId());
            cs2.executeQuery();
            addHeroesAndItemsToSelection(selection);
            selections.put(selection.getId(),selection);
        }
        cs.close();



        ArrayList<Selection> result = new ArrayList<>();
        for(Integer key : selections.keySet()){
            result.add(selections.get(key));
        }
        return result;
    }

    public Selection getSelection(Integer id)throws SQLException, DbAccessException{

        CallableStatement cs = getConnection().prepareCall(sqlSelectSelection);
        cs.setInt(2,id);
        cs.registerOutParameter(1,OracleTypes.CURSOR);
        cs.executeQuery();
        ResultSet rs = cs.getObject(1,ResultSet.class);
        if(rs.next()){
            Selection selection = new Selection();
            selection.setId(rs.getInt("id"));
            selection.setSelectionName(rs.getString("name"));
            selection.setDurationMin(rs.getInt(3));
            selection.setDurationMax(rs.getInt(4));
            selection.setPatchMin(rs.getInt(5));
            selection.setPatchMax(rs.getInt(6));
            selection.setDateMin(rs.getDate(7));
            selection.setDateMax(rs.getDate(8));
            selection.setHero(SuperService.getHeroes().get(rs.getInt(9)));

            addHeroesAndItemsToSelection(selection);
            return selection;
        }else {
            throw new DbAccessException("Error with query");
        }
    }

    void addHeroesAndItemsToSelection(Selection selection)throws SQLException, DbAccessException{
        CallableStatement cs2 =getConnection().prepareCall(sqlSelectSelectionsHeroes);
        cs2.registerOutParameter(1,OracleTypes.CURSOR);
        cs2.setInt(2,selection.getId());
        cs2.executeQuery();
        ResultSet rs2 = cs2.getObject(1,ResultSet.class);
        while(rs2.next()){
            String her = rs2.getString(3);
            if (rs2.getString(3).equals("enemy")){
                selection.enemies.add(SuperService.getHeroes().get(rs2.getInt(2)));
            }else  if (rs2.getString(3).equals("ally")){
                selection.allies.add(SuperService.getHeroes().get(rs2.getInt(2)));
            }
        }
        rs2.close();
        cs2.close();

        cs2 =getConnection().prepareCall(sqlSelectSelectionsItems);
        cs2.registerOutParameter(1,OracleTypes.CURSOR);
        cs2.setInt(2,selection.getId());
        cs2.executeQuery();
        rs2 = cs2.getObject(1,ResultSet.class);
        while(rs2.next()){
            if (rs2.getString(3).equals("boughtItem")){
                selection.items.add(SuperService.getItems().get(rs2.getInt(2)));
            }
        }
        rs2.close();
        cs2.close();
    }
}
