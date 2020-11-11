package com.lojka.kurs.testing;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DbConnectionClosedException;
import com.lojka.kurs.exception.DotaApiException;
import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.HeroRole;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.repository.IDbConnector;
import com.lojka.kurs.repository.IDbRepository;
import com.lojka.kurs.repository.oracle.OracleDbConnector;
import com.lojka.kurs.service.super_service.EFilterForMatchInserting;
import com.lojka.kurs.service.super_service.SuperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;
//import oracle.database.jdbc.*;

@Slf4j
public class Testing {
    //testin super service
    static {
        SuperService sp = new SuperService();
//        try {
////            if (!SuperService.updateDotaInfoFromApi()){
////                SuperService.updateDotaInfoFromApi();
////            }
////            SuperService.insertMatch(5661553712l);
////            Boolean cycle = true;
////            while (cycle){
////                try{
////                    SuperService.insertMatch(2526759078l);
////                    SuperService.insertMatches(EFilterForMatchInserting.professionalsEarlier);
////                }catch (HttpClientErrorException e){
////                    try {
////                        log.error("429\nsleeping for a minute");
////                        Thread.sleep(60000);
////                    } catch (InterruptedException ex) {
////                        cycle=false;
////                        ex.printStackTrace();
////                    }
////                }
////            }
//        } catch (DbAccessException e) {
//            log.error("DbAccessException: "  + e.getMessage());
//            e.printStackTrace();
//        } catch (DotaDataAccessException e) {
//            log.error("DotaDataAccessException: " + e.getMessage());
//            e.printStackTrace();
//        } catch (DbConnectionClosedException e) {
//            log.error("DbConnectionClosedException: " + e.getMessage());
//            e.printStackTrace();
//        }
    }


    //inserting matches
//    static {
//        IDbConnector connector = new OracleDbConnector();
//        try {
//            IDbRepository rep = connector.getRepository();
//            Map<Integer, Item> items = rep.getItems();
//            Map<Integer, Hero> heroes = rep.getHeroes();
//            Map<Integer, HeroRole> roles = rep.getHeroRoles();
//            rep.addHeroesRolesToHeroes(heroes,roles);
//            Hero h = heroes.get(4);
//            heroes.get(5);
//        } catch (DbAccessException e) {
//            e.printStackTrace();
//        }
////        catch (DotaDataAccessException e) {
////            e.printStackTrace();
////        }
//    }
//    //inserting matches
//    static {
//        IDbConnector connector = new OracleDbConnector();
//        try {
//            IDotaDataResource dataResource;
//            dataResource = new OpenDotaDataResource();
//            Match m = dataResource.getMatch(5651414501L);
//            IDbRepository rep = connector.getRepository();
//            rep.insertMatch(m);
//        } catch (DbAccessException e) {
//            e.printStackTrace();
//        } catch (DotaDataAccessException e) {
//            e.printStackTrace();
//        }
//    }
    //db inserting constatnts
//    static {
//        IDbConnector connector = new OracleDbConnector();
//        try {
//            IDotaDataResource dataResource;
//            dataResource = new OpenDotaDataResource();
//            Hero[] heroes = dataResource.getAllHeroes();
//            Item[] items = dataResource.getAllItems();
//            IDbRepository rep = connector.getRepository();
//            rep.updateItems(items);
//            HeroRole[] roles = HeroRoleFactory.getRoles().toArray(new HeroRole[0]);
//            rep.updateHeroRoles(roles);
//            rep.updateHeroes(heroes);
//        } catch (DbAccessException e) {
//            e.printStackTrace();
//        } catch (DotaDataAccessException e) {
//            e.printStackTrace();
//        }
//    }
//    //get requests from dotaapi
//    static {
//        IDotaDataResource dataResource;
//        dataResource = new OpenDotaDataResource();
//        log.info("testing...");
//        try {
//            //Match m1 = dataResource.getMatch(-1l);
//            Match m = dataResource.getRecentMatches()[0];
//            Item[] items = dataResource.getAllItems();
//            Object ads =  null;
//        } catch (DotaDataAccessException e) {
//            e.printStackTrace();
//        }
//    }

    //to get json to create oracle table
//    static {
//        IDotaDataResource dataResource;
//        dataResource = new OpenDotaDataResource();
//        log.info("testing...");
//        try {
//            Match m = dataResource.getRecentMatches()[0];
//            StringWriter writer = new StringWriter();
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.writeValue(writer, m);
//            String result = writer.toString();
//            System.out.println(result);
//        } catch (DotaDataAccessException e) {
//            e.printStackTrace();
//        } catch (JsonGenerationException e) {
//            e.printStackTrace();
//        } catch (JsonMappingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
