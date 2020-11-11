package com.lojka.kurs.service.super_service;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DbConnectionClosedException;
import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.*;
import com.lojka.kurs.model.queriesV2.*;
import com.lojka.kurs.model.queriesV2.bar.BarChart;
import com.lojka.kurs.model.queriesV2.bar.BarData;
import com.lojka.kurs.model.queriesV2.bar.BarDataSet;
import com.lojka.kurs.model.queriesV2.bubble.BubbleChart;
import com.lojka.kurs.model.queriesV2.bubble.BubbleCoord;
import com.lojka.kurs.model.queriesV2.bubble.BubbleData;
import com.lojka.kurs.model.queriesV2.bubble.BubbleDataSet;
import com.lojka.kurs.model.queriesV2.linee_chart.*;
import com.lojka.kurs.repository.IDbConnector;
import com.lojka.kurs.repository.IDbRepository;
import com.lojka.kurs.repository.oracle.OracleDbConnector;
import com.lojka.kurs.service.dota_data_access.IDotaDataResource;
import com.lojka.kurs.service.dota_data_access.open_api.OpenDotaDataResource;
import com.lojka.kurs.service.factory.HeroRoleFactory;
import lombok.extern.slf4j.Slf4j;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

    //bubble chart
    public static BubbleDataSet setBubbleDataSet(BubbleDataSet dataset, BubbleChart chart, Selection selection) throws DbAccessException, Exception{
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
            query += "join pdb_kurs_dwh_admin.playersmatches allies on my.match_id = allies.match_id and allies.win = my.win ";
        }
        if (selection.enemies.size()!=0){
            query += "join pdb_kurs_dwh_admin.playersmatches enemies on my.match_id = enemies.match_id and enemies.win <> my.win ";
        }
        if (selection.items.size()!=0){
            query += "join pdb_kurs_dwh_admin.boughtitems bItems on my.player_match_id = bItems.player_match_id ";
        }


        //conditions
        query += " where my.hero_id=" + selection.hero.getId().toString() + " ";

        //enemies and allies
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
        index = 0;
        if (selection.items.size() != 0){
            query+=" and (";
            for(Item item : selection.items){
                if (index != 0){
                    query+=" or ";
                }
                query += " bItems.item_id = " + item.getId();
                index++;
            }
            query+=")";
        }
        //selections conditions
        query += " and  my.durationmin between " + selection.durationMin + " and " + selection.durationMax + " ";
        query += " and  my.version between " + selection.patchMin + " and " + selection.patchMax + " ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        query += " and my.START_DATE between '" + dateFormat.format(selection.dateMin)  + "' and  '" + dateFormat.format(selection.dateMax) + "' ";

        //grouping
        query += " group by floor(my.";
        query += chart.xAxis + "/" + scale.toString() + ")*" + scale.toString();
        query += " having count(*) > " + chart.minCountOfMatches;
        query += " order by xAxis";


        ResultSet rs = rep.executeQuery(query);
        Float maxBubbleSize = 15f;
        Float minBubbleSize = 2f;
        Integer maxSelection = 50;
        while(rs.next()){
            dataset.data.add(new BubbleCoord(
                    rs.getFloat(1),
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
                    setBubbleDataSet(new BubbleDataSet(selection.selectionName), bubbleChart, selection)
            );
        }
        return bubbleData;
    }

    //bars
    public static BarDataSet setBarDataSet(BarDataSet dataset, BarChart chart, Selection selection)throws Exception{
        log.debug("setBarData");
        //selection
        String query = "select ";
        query += "xAxis.name, ";
        switch (chart.yAxis){
            case "kda":{
                query += "sum(my.kills+my.assists)/(sum(my.deaths)+1) as yAxis, ";
            }
            default:{
                query += "avg(my." + chart.yAxis + ") as yAxis, ";
            }
        }
        query += " count(*) as selection ";
        //join for hero_id of enemies and allies
        query += "from pdb_kurs_dwh_admin.playersmatches my ";
        if (selection.allies.size()!=0 || chart.xAxis=="allies"){
            query += "join pdb_kurs_dwh_admin.playersmatches allies on my.match_id = allies.match_id and allies.win = my.win ";
        }
        if (selection.enemies.size()!=0 || chart.xAxis=="enemies"){
            query += "join pdb_kurs_dwh_admin.playersmatches enemies on my.match_id = enemies.match_id and enemies.win <> my.win ";
        }
        if (selection.items.size()!=0 || chart.xAxis=="items"){
            query += "join pdb_kurs_dwh_admin.boughtitems bItems on my.player_match_id = bItems.player_match_id ";
        }

        //join for getting names of heroes of enemies and allies
        if (chart.xAxis == "enemies"){
            query += "join pdb_kurs_dwh_admin.heroes xAxis on enemies.hero_id = xAxis.id ";
        }else if (chart.xAxis == "allies"){
            query += "join pdb_kurs_dwh_admin.heroes xAxis on allies.hero_id = xAxis.id ";
        }else if (chart.xAxis == "items"){
            query += "join pdb_kurs_dwh_admin.items xAxis on bItems.item_id = xAxis.id ";
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
        index = 0;
        if (selection.items.size() != 0){
            query+=" and (";
            for(Item item : selection.items){
                if (index != 0){
                    query+=" or ";
                }
                query += " bItems.item_id = " + item.getId();
                index++;
            }
            query+=")";
        }

        //selections conditions
        query += " and  my.durationmin between " + selection.durationMin + " and " + selection.durationMax + " ";
        query += " and  my.version between " + selection.patchMin + " and " + selection.patchMax + " ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        query += " and  my.START_DATE between '" + dateFormat.format(selection.dateMin)  + "' and  '" + dateFormat.format(selection.dateMax) + "' ";

        query += " and(";
        index = 0;
        for(String filterLabel : chart.xLabels){
            if (index++!=0)
                query+=" or " ;
            query+="xAxis.name = '" + filterLabel +"'";
        }
        query += ") ";

        //grouping
        query += "group by xAxis.name";
        query += " having count(*) > " + chart.minCountOfMatches;



        ResultSet rs = rep.executeQuery(query);
        Float maxBubbleSize = 15f;
        Float minBubbleSize = 2f;
        Integer maxSelection = 50;
        while(rs.next()){
            dataset.mapData.put(
                    rs.getString(1),
                    rs.getFloat(2)
            );
        }
        dataset.createDataFromMap(chart.xLabels);
        return dataset;
    }

    public static BarData getBarData(BarChart chart) throws Exception{
        BarData barData = new BarData();
        for(Selection selection : chart.selections){
            barData.datasets.add(
                    setBarDataSet(new BarDataSet(selection.selectionName), chart, selection)
            );
        }
        barData.labels = chart.xLabels;
        barData.name = chart.name;
        return barData;
    }


    //line chart
    public static void setLineDataSet(LineData lineData, LineDataSet dataset, LineDataSet selectionDataSet, LineChart chart, Selection selection)throws Exception{
        log.debug("setLineData");
        //selection
        String query = "select * from (select ";
        query += "xAxis.name, ";
        switch (chart.yAxis){
            case "kda":{
                query += "sum(my.kills+my.assists)/(sum(my.deaths)+1) as yAxis, ";
            }
            default:{
                query += "avg(my." + chart.yAxis + ") as yAxis, ";
            }
        }
        query += " count(*) as selection ";
        //join for hero_id of enemies and allies
        query += "from pdb_kurs_dwh_admin.playersmatches my ";
        if (selection.allies.size()!=0 || chart.xAxis=="allies"){
            query += "join pdb_kurs_dwh_admin.playersmatches allies on my.match_id = allies.match_id and allies.win = my.win ";
        }
        if (selection.enemies.size()!=0 || chart.xAxis=="enemies"){
            query += "join pdb_kurs_dwh_admin.playersmatches enemies on my.match_id = enemies.match_id and enemies.win <> my.win ";
        }
        if (selection.items.size()!=0 || chart.xAxis=="items"){
            query += "join pdb_kurs_dwh_admin.boughtitems bItems on my.player_match_id = bItems.player_match_id ";
        }

        //join for getting names of heroes of enemies and allies
        if (chart.xAxis == "enemies"){
            query += "join pdb_kurs_dwh_admin.heroes xAxis on enemies.hero_id = xAxis.id ";
        }else if (chart.xAxis == "allies"){
            query += "join pdb_kurs_dwh_admin.heroes xAxis on allies.hero_id = xAxis.id ";
        }else if (chart.xAxis == "items"){
            query += "join pdb_kurs_dwh_admin.items xAxis on bItems.item_id = xAxis.id ";
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
        index = 0;
        if (selection.items.size() != 0){
            query+=" and (";
            for(Item item : selection.items){
                if (index != 0){
                    query+=" or ";
                }
                query += " bItems.item_id = " + item.getId();
                index++;
            }
            query+=")";
        }

        //selections conditions
        query += " and  my.durationmin between " + selection.durationMin + " and " + selection.durationMax + " ";
        query += " and  my.version between " + selection.patchMin + " and " + selection.patchMax + " ";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        query += " and  my.START_DATE between '" + dateFormat.format(selection.dateMin)  + "' and  '" + dateFormat.format(selection.dateMax) + "' ";


        //grouping
        query += "group by xAxis.name";
        query += " having count(*) > " + chart.minCountOfMatches;

        query += " order by yAxis ";
        if (chart.isDeasc)
            query+="desc " ;

        query += " ) where rownum <= " + chart.countOfLabels;

        ResultSet rs = rep.executeQuery(query);
        Float maxBubbleSize = 15f;
        Float minBubbleSize = 2f;
        Integer maxSelection = 50;
        while(rs.next()){
            lineData.labels.add(rs.getString(1));
            dataset.mapData.put(
                    rs.getString(1),
                    rs.getFloat(2)
            );
            selectionDataSet.mapData.put(
                    rs.getString(1),
                    rs.getFloat(3)
            );
        }
        dataset.createDataFromMap(lineData.labels);
        selectionDataSet.createDataFromMap(lineData.labels);
    }

    public static LineData getLineData(LineChart chart) throws Exception{
        LineData lineData = new LineData();
        LineDataSet dataSet =new LineDataSet(chart.selection.selectionName);
        LineDataSet selectionDataSet = new LineDataSet(chart.selection.selectionName + " count of matches");
        lineData.datasets.add(dataSet);
        lineData.datasets.add(selectionDataSet);
        setLineDataSet(
                lineData,
                dataSet,
                selectionDataSet,
                chart, chart.selection);
        lineData.name = chart.name;
        return lineData;
    }
}
