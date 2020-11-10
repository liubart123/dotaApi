package com.lojka.kurs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojka.kurs.model.queriesV2.bar.BarChart;
import com.lojka.kurs.model.queriesV2.bar.BarData;
import com.lojka.kurs.model.queriesV2.bubble.BubbleChart;
import com.lojka.kurs.model.queriesV2.bubble.BubbleData;
import com.lojka.kurs.model.queriesV2.Selection;
import com.lojka.kurs.service.super_service.SuperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Controller
public class TestController {
    @GetMapping(value = "/GetTest")
    public ModelAndView getTest() throws ParseException {
        ModelAndView mov = new ModelAndView();
        BubbleChart chart = new BubbleChart();
        chart.name="testing chart";
        chart.xAxis = "CREEP_KILLS";
        chart.yAxis = "LAST_GPM_SUM";
        chart.xScale = 10f;
        chart.minCountOfMatches = 5;
        Selection s1 = new Selection();
        s1.hero = SuperService.getHeroes().get(1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        s1.dateMax = formatter.parse("31.12.2020");
        s1.dateMin = formatter.parse("01.06.2020");
        s1.selectionName = "first selection";

        Selection s2 = new Selection();
        s2.hero = SuperService.getHeroes().get(1);
        s2.selectionName = "second selection";
        s2.dateMax = formatter.parse("01.6.2020");

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
        mov.setViewName("Analytic/BubbleChart");
        return mov;
    }


    @GetMapping(value = "/GetTestBar")
    public ModelAndView getTestBar(){
        ModelAndView mov = new ModelAndView();
        BarChart chart = new BarChart();
        chart.xLabels.add("Ember Spirit");
        chart.xLabels.add("Tiny");
        chart.xLabels.add("Earthshaker");
        chart.xLabels.add("Grimstroke");
        chart.xAxis = "allies";
        chart.yAxis = "LAST_GPM_PROPORTION";
        chart.name = "test bar";

        Selection s1 = new Selection();
        s1.hero = SuperService.getHeroes().get(86);
        s1.selectionName = "rubick and puck, void spirit";
//        s1.allies.add(SuperService.getHeroes().get(19));
//        s1.allies.add(SuperService.getHeroes().get(126));
        s1.enemies.add(SuperService.getHeroes().get(19));
        s1.enemies.add(SuperService.getHeroes().get(126));

        Selection s2 = new Selection();
        s2.hero = SuperService.getHeroes().get(86);
        s2.selectionName = "rubick";

        chart.selections.add(s1);
        chart.selections.add(s2);


        BarData data = null;
        try {
            data = SuperService.getBarData(chart);
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
        mov.setViewName("Analytic/BarChart");
        return mov;
    }
}
