package com.lojka.kurs;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.Match;
import com.lojka.kurs.repository.IDbRepository;
import com.lojka.kurs.repository.oracle.OracleDbRepository;
import com.lojka.kurs.service.dota_data_access.IDotaDataResource;
import com.lojka.kurs.service.dota_data_access.open_api.OpenDotaDataResource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

@SpringBootTest
class KursApplicationTests {

    @Test
    void contextLoads() {
    }


}
