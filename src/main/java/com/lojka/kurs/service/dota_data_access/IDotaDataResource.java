package com.lojka.kurs.service.dota_data_access;

import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.Match;

public interface IDotaDataResource {
    Match getMatch(Long id) throws DotaDataAccessException;
    Match[] getRecentMatches() throws DotaDataAccessException;
    Match[] getRecentProMatches() throws DotaDataAccessException;

}
