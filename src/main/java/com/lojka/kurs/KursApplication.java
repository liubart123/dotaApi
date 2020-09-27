package com.lojka.kurs;

import com.lojka.kurs.testing.Testing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KursApplication {

    public static void main(String[] args) {
        SpringApplication.run(KursApplication.class, args);
    }
    Testing t = new Testing();
}
