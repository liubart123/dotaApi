package com.lojka.kurs.testing;

import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.Match;
import com.lojka.kurs.service.dota_data_access.IDotaDataResource;
import com.lojka.kurs.service.dota_data_access.open_api.OpenDotaDataResource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Testing {
    static {
        IDotaDataResource dataResource;
        dataResource = new OpenDotaDataResource();
        log.info("testing...");
        try {
            //Match m1 = dataResource.getMatch(-1l);
            Match m = dataResource.getRecentMatches()[0];
        } catch (DotaDataAccessException e) {
            e.printStackTrace();
        }
    }
}
