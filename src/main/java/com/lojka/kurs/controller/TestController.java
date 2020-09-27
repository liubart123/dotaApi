package com.lojka.kurs.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
@Controller
public class TestController {
    @GetMapping(value = "/GetTest")
    public void getTest(){
//        URL obj = null;
//        try {
//            obj = new URL(url);
//            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
//            connection.setRequestMethod("GET");
//            //connection.setRequestProperty("Content-Type", "application/json");
//            InputStream is = connection.getInputStream();
//            BufferedReader in = new BufferedReader(new InputStreamReader(is));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//        } catch (Exception e) {
////            log.error();
//            e.printStackTrace();
//        }

//        ModelAndView modelAndView = new ModelAndView("GetTest");
//        return modelAndView;
    }
}
