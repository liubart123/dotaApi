package com.lojka.kurs.service.super_service;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DbConnectionClosedException;
import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.*;
import com.lojka.kurs.repository.IDbConnector;
import com.lojka.kurs.repository.IDbRepository;
import com.lojka.kurs.repository.oracle.OracleDbConnector;
import com.lojka.kurs.service.dota_data_access.IDotaDataResource;
import com.lojka.kurs.service.dota_data_access.open_api.OpenDotaDataResource;
import com.lojka.kurs.service.factory.HeroRoleFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
            case professionals:{break;}//TODO: filters for mathces inserting
        }
        return true;
    }

    //reach match data by items instances
    static void reachMatchData(Match match){
        for (PlayerInMatch pl:
             match.getPlayers()) {
            for (BoughtItem item:
                 pl.getPurchase_log()) {
                item.setItemId(itemsByName.get(item.getKey()).getId());
            }
        }
    }
}
