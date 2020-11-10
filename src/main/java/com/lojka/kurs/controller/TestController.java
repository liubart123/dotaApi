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
        mov.setViewName("BubbleChart");
        return mov;
    }


    @GetMapping(value = "/GetTestBar")
    public ModelAndView getTestBar(){
        ModelAndView mov = new ModelAndView();
        BarChart chart = new BarChart();
        chart.xLabels.add("Lycan");
        chart.xLabels.add("Meepo");
        chart.xLabels.add("Night Stalker");
        chart.xLabels.add("Vengeful Spirit");
        chart.xAxis = "enemies";
        chart.yAxis = "win";
        chart.name = "test bar";

        Selection s1 = new Selection();
        s1.hero = SuperService.getHeroes().get(1);
        s1.selectionName = "antimage with venga";
        s1.allies.add(SuperService.getHeroes().get(19));
        s1.allies.add(SuperService.getHeroes().get(2));
        s1.allies.add(SuperService.getHeroes().get(45));

        Selection s2 = new Selection();
        s2.hero = SuperService.getHeroes().get(1);
        s2.selectionName = "antimage";

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
