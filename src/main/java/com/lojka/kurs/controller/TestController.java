package com.lojka.kurs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojka.kurs.model.queriesV2.BubbleChart;
import com.lojka.kurs.model.queriesV2.BubbleData;
import com.lojka.kurs.model.queriesV2.BubbleDataSet;
import com.lojka.kurs.model.queriesV2.Selection;
import com.lojka.kurs.service.super_service.SuperService;
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
    public ModelAndView getTest(){
        ModelAndView mov = new ModelAndView();
        BubbleChart chart = new BubbleChart();
        chart.name="testing chart";
        chart.xAxis = "DURATIONMIN";
        chart.yAxis = "win";
        chart.xScale = 3f;
        Selection s1 = new Selection();
        s1.hero = SuperService.getHeroes().get(1);
        s1.selectionName = "first selection";
        s1.allies.add(SuperService.getHeroes().get(2));

        Selection s2 = new Selection();
        s2.hero = SuperService.getHeroes().get(1);
        s2.selectionName = "second selection";

        chart.selections.add(s1);
        chart.selections.add(s2);


        BubbleData data = null;
        try {
            data = SuperService.getBubbleData(chart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(data);
            mov.addObject("chartData", json );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        mov.addObject("yAxis", chart.yAxis);
        mov.addObject("xAxis", chart.xAxis);
        mov.setViewName("Analytic/Chart");
        return mov;
    }
}
