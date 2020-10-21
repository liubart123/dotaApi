package com.lojka.kurs;

import com.lojka.kurs.testing.Testing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class KursApplication {

    public static void main(String[] args) {
        log.trace("aplications running");
        SpringApplication.run(KursApplication.class, args);
    }
    Testing t = new Testing();
}
