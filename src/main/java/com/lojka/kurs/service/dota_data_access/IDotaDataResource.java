package com.lojka.kurs.service.dota_data_access;

import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.HeroRole;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.Match;

import java.util.ArrayList;
import java.util.List;
//getting info from remote api
public interface IDotaDataResource {
    Match getMatch(Long id) throws DotaDataAccessException;
    ArrayList<Match> getRecentMatches() throws DotaDataAccessException;
    ArrayList<Match> getRecentProMatches() throws DotaDataAccessException;
    ArrayList<Match> getEarlyProMatches(Long biggestMatchId) throws DotaDataAccessException;
    Hero[] getAllHeroes() throws DotaDataAccessException;
    Item[] getAllItems() throws DotaDataAccessException;
    List<HeroRole> getAllHeroRoles() throws DotaDataAccessException;
}
