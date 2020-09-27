package com.lojka.kurs.service.dota_data_access.open_api;

import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.Match;
import com.lojka.kurs.service.dota_data_access.IDotaDataResource;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class OpenDotaDataResource implements IDotaDataResource {
    static int maxCountOfRequests= 10;
    static String userAgent="Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36";
    static String basicUrl =  "https://api.opendota.com/api/";
    static String matchUrl = "matches/";
    static String recentMatchesUrl = "parsedMatches";
    static String recentProMatchesUrl = "proMatches";
    private String getQueryUrl(String param) throws DotaDataAccessException{
        switch (param){
            case "match" : return basicUrl + matchUrl;
            case "matches" : return basicUrl + recentMatchesUrl;
            case "proMatches" : return basicUrl + recentProMatchesUrl;
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
}
