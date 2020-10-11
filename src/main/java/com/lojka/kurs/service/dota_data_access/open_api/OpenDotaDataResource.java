package com.lojka.kurs.service.dota_data_access.open_api;

import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.HeroRole;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.Match;
import com.lojka.kurs.service.dota_data_access.IDotaDataResource;
import org.json.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenDotaDataResource implements IDotaDataResource {
    static int maxCountOfRequests= 2;
    static String userAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36";
    static String basicUrl =  "https://api.opendota.com/api/";
    static String matchUrl = "matches/";
    static String recentMatchesUrl = "parsedMatches";
    static String recentProMatchesUrl = "proMatches";
    static String heroesUrl = "constants/heroes";
    static String items = "constants/items";
    private String getQueryUrl(String param) throws DotaDataAccessException{
        switch (param){
            case "match" : return basicUrl + matchUrl;
            case "matches" : return basicUrl + recentMatchesUrl;
            case "proMatches" : return basicUrl + recentProMatchesUrl;
            case "heroes" : return basicUrl + heroesUrl;
            case "items" : return basicUrl + items;
            default: return null;
        }
    }
    @Override
    public Match getMatch(Long id) throws DotaDataAccessException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.set("user-agent",userAgent);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<Match> response = restTemplate.exchange(
                getQueryUrl("match") + id.toString(),
                HttpMethod.GET,
                request,
                Match.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Match m = response.getBody();
            return m;
        } else {
            throw new DotaDataAccessException("request failed");
        }
    }

    @Override
    public Match[] getRecentMatches() throws DotaDataAccessException{
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("user-agent",userAgent);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<MatchFromApi[]> response = restTemplate.exchange(
                getQueryUrl("matches"),
                HttpMethod.GET,
                request,
                MatchFromApi[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            MatchFromApi[] ids = response.getBody();
            Match[] result = new Match[ids.length];
            for (int i=0;i<ids.length && i <maxCountOfRequests;i++){
                try{
                    result[i]=getMatch(ids[i].match_id);
                }catch (DotaDataAccessException e){
                    if (i!=0){
                        return result;
                    }else {
                        throw e;
                    }
                }catch (Exception e){
                    throw e;
                }
            }
            return result;
        } else {
            throw new DotaDataAccessException("request failed");
        }
    }

    @Override
    public Match[] getRecentProMatches() throws DotaDataAccessException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("user-agent",userAgent);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<ProMatchFromApi[]> response = restTemplate.exchange(
                getQueryUrl("matches"),
                HttpMethod.GET,
                request,
                ProMatchFromApi[].class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            ProMatchFromApi[] ids = response.getBody();
            Match[] result = new Match[ids.length];
            for (int i=0;i<ids.length && i <maxCountOfRequests;i++){
                try{
                    result[i]=getMatch(ids[i].match_id);
                }catch (DotaDataAccessException e){
                    if (i!=0){
                        return result;
                    }else {
                        throw e;
                    }
                }
            }
            return result;
        } else {
            throw new DotaDataAccessException("request failed");
        }
    }

    @Override
    public Hero[] getAllHeroes() throws DotaDataAccessException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("user-agent",userAgent);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> jsonHeroes = restTemplate.exchange(
                getQueryUrl("heroes"),
                HttpMethod.GET,
                request,
                String.class
        );


        if (jsonHeroes.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = new JSONObject(jsonHeroes.getBody());
            Map<String,Object> map = jsonObject.toMap();
            Hero[] result = new Hero[map.size()];
            Object[] keys = (Object[])(map.keySet().toArray());
            for (int i=0;i<map.size();i++){
                Hero hero = new Hero();
                Map<String, Object> heroAttributes = (Map<String, Object>)map.get(keys[i]);
                hero.setId((Integer)heroAttributes.get("id"));
                hero.setName((String)heroAttributes.get("localized_name"));
                List<HeroRole> roles = new ArrayList<>();
                for (String role:
                        (ArrayList<String>)heroAttributes.get("roles")) {
                    roles.add(new HeroRole(role));
                }
                hero.setRoles(roles);
                result[i] = hero;
            }
            return result;
        } else {
            throw new DotaDataAccessException("request failed");
        }
    }
    @Override
    public Item[] getAllItems() throws DotaDataAccessException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("user-agent",userAgent);
        HttpEntity request = new HttpEntity(headers);
        ResponseEntity<String> json = restTemplate.exchange(
                getQueryUrl("items"),
                HttpMethod.GET,
                request,
                String.class
        );


        if (json.getStatusCode() == HttpStatus.OK) {
            JSONObject jsonObject = new JSONObject(json.getBody());
            Map<String,Object> map = jsonObject.toMap();
            Item[] result = new Item[map.size()];
            Object[] keys = (Object[])(map.keySet().toArray());
            for (int i=0;i<map.size();i++){
                Item item = new Item();
                Map<String, Object> itemAttributes = (Map<String, Object>)map.get(keys[i]);
                item.setId((Integer)itemAttributes.get("id"));
                item.setName((String)itemAttributes.get("dname"));
                String desc = "";
                ArrayList<String> hints = (ArrayList<String>)itemAttributes.get("hint");
                if (hints!=null){
                    for (String hint :
                            hints) {
                        desc += hint + "\n";
                    }
                }
                desc += itemAttributes.get("notes");

                ArrayList<Map<String,Object>> attributes = (ArrayList<Map<String,Object>> )itemAttributes.get("attrib");
                for (Map<String,Object> atr:
                        attributes) {
                    desc+= "\n* " + atr.get("footer") + " " + atr.get("header") + " " + atr.get("value");
                }
                if (itemAttributes.get("cd").getClass() != Boolean.class){
                    desc += "\ncd: " + (Integer)itemAttributes.get("cd");
                }
                if (itemAttributes.get("mc").getClass() != Boolean.class){
                    desc += "\nmanacost: " + (Integer)itemAttributes.get("mc");
                }
                desc += "\ncost: " + (Integer)itemAttributes.get("cost");
                item.setDescription(desc);
                result[i] = item;
            }
            return result;
        } else {
            throw new DotaDataAccessException("request failed");
        }
    }
}