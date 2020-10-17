package com.lojka.kurs.service.super_service;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.*;
import com.lojka.kurs.repository.IDbConnector;
import com.lojka.kurs.repository.IDbRepository;
import com.lojka.kurs.repository.oracle.OracleDbConnector;
import com.lojka.kurs.service.dota_data_access.IDotaDataResource;
import com.lojka.kurs.service.dota_data_access.open_api.OpenDotaDataResource;
import com.lojka.kurs.service.factory.HeroRoleFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        updateConstants();
    }
    //updating constants from db
    static void updateConstants(){
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
        } catch (DbAccessException e) {
            log.error("error with creating static resources of super_service: " + e.getMessage());
//            e.printStackTrace();
        }
    }

    //updating dota constants such as heroes or items from remote server
    public static void updateDotaInfoFromApi() throws DbAccessException, DotaDataAccessException{
        log.debug("update dota info from api");
        Hero[] heroes = dotaDataResource.getAllHeroes();
        Item[] items = dotaDataResource.getAllItems();
        rep.updateItems(items);
        HeroRole[] roles = HeroRoleFactory.getRoles().toArray(new HeroRole[0]);
        rep.updateHeroRoles(roles);
        rep.updateHeroes(heroes);
        updateConstants();
    }

    //insert match from remote api to db
    public static void insertMatch(Long id)throws DbAccessException, DotaDataAccessException{
        Match m = dotaDataResource.getMatch(id);
        reachMatchData(m);
        rep.insertMatch(m);
    }
    //insert match from remote api to db
    public static void insertMatches(EFilterForMatchInserting filter)throws DbAccessException, DotaDataAccessException{
        switch (filter){
            case allMatches:{
                ArrayList<Match> mathces = dotaDataResource.getRecentMatches();
                for (Match m:
                        mathces) {
                    reachMatchData(m);
                    rep.insertMatch(m);
                }
                break;
            }
            case professionals:{break;}//TODO: filters for mathces inserting
        }
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
