package com.lojka.kurs.testing;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.HeroRole;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.repository.IDbConnector;
import com.lojka.kurs.repository.IDbRepository;
import com.lojka.kurs.repository.oracle.OracleDbConnector;
import com.lojka.kurs.service.dota_data_access.IDotaDataResource;
import com.lojka.kurs.service.dota_data_access.open_api.OpenDotaDataResource;
import com.lojka.kurs.service.factory.HeroRoleFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
//import oracle.database.jdbc.*;

@Slf4j
public class Testing {
    //db access test
    static {
        IDbConnector connector = new OracleDbConnector();
        try {
            IDotaDataResource dataResource;
            dataResource = new OpenDotaDataResource();
            Hero[] heroes = dataResource.getAllHeroes();
            Item[] items = dataResource.getAllItems();
            IDbRepository rep = connector.getRepository();
            rep.updateItems(items);
            HeroRole[] roles = HeroRoleFactory.getRoles().toArray(new HeroRole[0]);
            rep.updateHeroRoles(roles);
            rep.updateHeroes(heroes);
        } catch (DbAccessException e) {
            e.printStackTrace();
        } catch (DotaDataAccessException e) {
            e.printStackTrace();
        }
    }
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
