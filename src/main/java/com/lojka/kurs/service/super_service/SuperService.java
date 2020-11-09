package com.lojka.kurs.service.super_service;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DbConnectionClosedException;
import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.*;
import com.lojka.kurs.model.queriesV2.*;
import com.lojka.kurs.repository.IDbConnector;
import com.lojka.kurs.repository.IDbRepository;
import com.lojka.kurs.repository.oracle.OracleDbConnector;
import com.lojka.kurs.service.dota_data_access.IDotaDataResource;
import com.lojka.kurs.service.dota_data_access.open_api.OpenDotaDataResource;
import com.lojka.kurs.service.factory.HeroRoleFactory;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
//main service with all main functions. Controllers should interact with him
public class SuperService {
    static IDbRepository rep;
    static IDotaDataResource dotaDataResource;
    static Map<Integer, Item> items;
    static Map<String, Item> itemsByName = new HashMap<>();
    static Map<Integer, Hero> heroes;
    static Map<Integer, HeroRole> roles;
    static IDbConnector connector;

    static{
        log.debug("super service initialisation");
        connector = new OracleDbConnector();
        dotaDataResource = new OpenDotaDataResource();
        try {
            if (!updateConstants()){
                updateConstants();
            }
        } catch (DbConnectionClosedException e) {
            log.error("FATAL ERROR. No connection to db!!!!");
        }
    }
    static Boolean repairConnection(){
        log.debug("super service repair connection");
        connector = new OracleDbConnector();
        try {
            updateConstants();
            return !connector.getRepository().isConnectionsClosed();
        }catch (DbConnectionClosedException e) {
            log.error("connection was closed during connection repairing ");
            return false;
        } catch (DbAccessException e) {
            return false;
        }
    }
    //updating constants from db
    static Boolean updateConstants() throws DbConnectionClosedException {
        try {
            rep = connector.getRepository();
            items = rep.getItems();
            itemsByName.clear();
            for (Map.Entry<Integer, Item> item:
                 items.entrySet()) {
                itemsByName.put(item.getValue().getKeyName(),item.getValue());
            }
            heroes = rep.getHeroes();
            roles = rep.getHeroRoles();
            rep.addHeroesRolesToHeroes(heroes,roles);
        } catch (DbConnectionClosedException e) {
            log.error("connection was closed");
            if (!repairConnection())
                throw e;
            return false;
        }catch (DbAccessException e) {
            log.error("error with creating static resources of super_service: " + e.getMessage());
//            e.printStackTrace();
        }
        return true;
    }

    //updating dota constants such as heroes or items from remote server
    public static Boolean updateDotaInfoFromApi() throws DbAccessException, DotaDataAccessException, DbConnectionClosedException {
        log.debug("update dota info from api");
        Hero[] heroes = dotaDataResource.getAllHeroes();
        Item[] items = dotaDataResource.getAllItems();
        try {
            rep.updateItems(items);
            HeroRole[] roles = HeroRoleFactory.getRoles().toArray(new HeroRole[0]);
            rep.updateHeroRoles(roles);
            rep.updateHeroes(heroes);
        } catch (DbConnectionClosedException e) {
            log.error("connection was closed");
            if (!repairConnection())
                throw e;
            return false;
        }
        updateConstants();
        return true;
    }

    //insert match from remote api to db
    public static Boolean insertMatch(Long id) throws DbAccessException, DotaDataAccessException, DbConnectionClosedException {
        Match m = dotaDataResource.getMatch(id);
        if (m==null)
            return false;
            //throw new DotaApiException("invalid data from dota api");
        reachMatchData(m);
        try {
            rep.insertMatch(m);
        } catch (DbConnectionClosedException e) {
            log.error("connection was closed");
            if (!repairConnection())
                throw e;
            return false;
        }
        return true;
    }
    //insert match from remote api to db
    public static Boolean insertMatches(EFilterForMatchInserting filter) throws DbAccessException, DotaDataAccessException, DbConnectionClosedException {
        switch (filter){
            case allMatches:{
                ArrayList<Match> mathces = dotaDataResource.getRecentMatches();
                for (Match m:
                        mathces) {
                    reachMatchData(m);
                    try {
                        rep.insertMatch(m);
                    } catch (DbConnectionClosedException e) {
                        log.error("connection was closed");
                        if (!repairConnection())
                            throw e;
                        return false;
                    }
                }
                break;
            }
            case professionals:{
                ArrayList<Match> mathces = dotaDataResource.getRecentProMatches();
                for (Match m:
                        mathces) {
                    reachMatchData(m);
                    try {
                        rep.insertMatch(m);
                    } catch (DbConnectionClosedException e) {
                        log.error("connection was closed");
                        if (!repairConnection())
                            throw e;
                        return false;
                    }
                }
                break;
            }
            case professionalsEarlier:{
                Long lowestMatchId;
                lowestMatchId = rep.getLowestMatchId();
                if (lowestMatchId==-1){
                    return false;
                }
                ArrayList<Match> mathces = dotaDataResource.getEarlyProMatches(lowestMatchId);
                for (Match m:
                        mathces) {
                    reachMatchData(m);
                    try {
                        rep.insertMatch(m);
                    } catch (DbConnectionClosedException e) {
                        log.error("connection was closed");
                        if (!repairConnection())
                            throw e;
                        return false;
                    }
                }
                break;
            }
        }
        return true;
    }

    //reach match data by items instances
    static void reachMatchData(Match match){
        log.trace("reaching match with items data. id: " + match.getMatch_id());
        for (PlayerInMatch pl:
             match.getPlayers()) {
            for (BoughtItem item:
                 pl.getPurchase_log()) {
                item.setItemId(itemsByName.get(item.getKey()).getId());
            }
        }
    }

    public static Map<Integer, Item> getItems() {
        return items;
    }

    public static Map<Integer, Hero> getHeroes() {
        return heroes;
    }

    public static Map<Integer, HeroRole> getRoles() {
        return roles;
    }

    public static BubbleData GetChart(
            Integer durationMin,
            Integer durationMax,
            Integer patchMin,
            Integer patchMax,
            Date dateMin,
            Date dateMax,
            String yAxis,
            String xAxis,
            List<Hero> allies,
            List<Hero> enemies,
            Hero investigatedHero
    ){
        Float scale = 3f;
        log.debug("getting chart");
        //selection
        String query = "select floor(my.";
        query += xAxis + "/" + scale.toString() + ")*" + scale.toString() + " as xAxis, ";
        switch (yAxis){
            case "kda":{
                query += "sum(my.kills+my.assists)/(sum(my.deaths)+1) as yAxis, ";
            }
            default:{
                query += "avg(my." + yAxis + ") as yAxis, ";
            }
        }
        query += " count(*) as selection ";


        //from
        query += "from pdb_kurs_dwh_admin.playersmatches my ";
        if (allies.size()!=0){
            query += "join pdb_kurs_dwh_admin.playersmatches allies on my.match_id = allies.match_id ";
        }
        if (enemies.size()!=0){
            query += "join pdb_kurs_dwh_admin.playersmatches enemies on my.match_id = enemies.match_id ";
        }


        //conditions
        query += " where my.hero_id=" + investigatedHero.getId().toString() + " ";

        Integer index = 0;
        if (allies.size() != 0){
            query+="and (";
            for(Hero ally : allies){
                if (index != 0){
                    query+=" or ";
                }
                query += " allies.hero_id = " + ally.getId();
                index++;
            }
            query+=")";
        }


        index = 0;
        if (enemies.size() != 0){
            query+=" and (";
            for(Hero enemy : enemies){
                if (index != 0){
                    query+=" or ";
                }
                query += " enemies.hero_id = " + enemy.getId();
                index++;
            }
            query+=")";
        }

        //grouping
        query += "group by floor(my.";
        query += xAxis + "/" + scale.toString() + ")*" + scale.toString();
        query += " order by xAxis";

        try {
            ResultSet rs = rep.executeQuery(query);
            BubbleData result = new BubbleData();
            result.datasets.add(new BubbleDataSet());
            Float maxBubbleSize = 10f;
            Integer maxSelection = 50;
            while(rs.next()){
                result.datasets.get(0).data.add(new BubbleCoord(
                        rs.getInt(1),
                        rs.getFloat(2),
                        Math.min(rs.getInt(3),maxSelection)/maxSelection.floatValue()*maxBubbleSize
                ));
            }

            return result;
        } catch (DbAccessException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (DbConnectionClosedException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    public static BubbleDataSet setBubbleData(BubbleDataSet dataset, BubbleChart chart, Selection selection) throws DbAccessException, Exception{
        Float scale = chart.xScale;
        log.debug("setBubbleData");
        //selection
        String query = "select floor(my.";
        query += chart.xAxis + "/" + scale.toString() + ")*" + scale.toString() + " as xAxis, ";
        switch (chart.yAxis){
            case "kda":{
                query += "sum(my.kills+my.assists)/(sum(my.deaths)+1) as yAxis, ";
            }
            default:{
                query += "avg(my." + chart.yAxis + ") as yAxis, ";
            }
        }
        query += " count(*) as selection ";
        //from
        query += "from pdb_kurs_dwh_admin.playersmatches my ";
        if (selection.allies.size()!=0){
            query += "join pdb_kurs_dwh_admin.playersmatches allies on my.match_id = allies.match_id ";
        }
        if (selection.enemies.size()!=0){
            query += "join pdb_kurs_dwh_admin.playersmatches enemies on my.match_id = enemies.match_id ";
        }


        //conditions
        query += " where my.hero_id=" + selection.hero.getId().toString() + " ";

        Integer index = 0;
        if (selection.allies.size() != 0){
            query+="and (";
            for(Hero ally : selection.allies){
                if (index != 0){
                    query+=" or ";
                }
                query += " allies.hero_id = " + ally.getId();
                index++;
            }
            query+=")";
        }


        index = 0;
        if (selection.enemies.size() != 0){
            query+=" and (";
            for(Hero enemy : selection.enemies){
                if (index != 0){
                    query+=" or ";
                }
                query += " enemies.hero_id = " + enemy.getId();
                index++;
            }
            query+=")";
        }

        //grouping
        query += "group by floor(my.";
        query += chart.xAxis + "/" + scale.toString() + ")*" + scale.toString();
        query += " order by xAxis";


        ResultSet rs = rep.executeQuery(query);
        Float maxBubbleSize = 15f;
        Float minBubbleSize = 2f;
        Integer maxSelection = 50;
        while(rs.next()){
            dataset.data.add(new BubbleCoord(
                    rs.getInt(1),
                    rs.getFloat(2),
                    Math.min(rs.getInt(3),maxSelection)/maxSelection.floatValue()*(maxBubbleSize-minBubbleSize)+minBubbleSize
            ));
        }
        return dataset;
    }
    //get bubble chart that will be drawed by js
    public static BubbleData getBubbleData(BubbleChart bubbleChart) throws DbAccessException, Exception{
        BubbleData bubbleData = new BubbleData();
        for(Selection selection : bubbleChart.selections){
            bubbleData.datasets.add(
                    setBubbleData(new BubbleDataSet(selection.selectionName), bubbleChart, selection)
            );
        }
        return bubbleData;
    }
}
