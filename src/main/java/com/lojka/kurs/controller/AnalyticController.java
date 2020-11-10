package com.lojka.kurs.controller;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.queriesV2.bubble.BubbleChart;
import com.lojka.kurs.model.queriesV2.bubble.BubbleData;
import com.lojka.kurs.model.queriesV2.Selection;
import com.lojka.kurs.service.super_service.SuperService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequestMapping("/analytic")
@RestController
public class AnalyticController {
    @GetMapping("/CreateSelection")
    ModelAndView getChartSettingsWindow(Model model){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("Analytic/CreateSelection");
        mov.addObject("heroes", SuperService.getHeroes());
        return mov;
    }
    @PostMapping("/CreateSelection")
    ModelAndView postBuildChart(Model model,
                                @RequestParam String selectionName,
                                @RequestParam Integer durationMin,
                                @RequestParam Integer durationMax,
                                @RequestParam Integer patchMin,
                                @RequestParam Integer patchMax,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateMin,
                                @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateMax,
                                @RequestParam String yAxis,
                                @RequestParam String xAxis,
                                @RequestParam String allies,
                                @RequestParam String enemies,
                                @RequestParam String hero
                                ){
        ModelAndView mov = new ModelAndView();
        Hero heroHero = SuperService.getHeroes().get(Integer.parseInt(hero));
        List<Hero> alliesHeroes = new ArrayList<>();
        for(String heroId : allies.split(",")){
            if (heroId=="")
                break;
            alliesHeroes.add(SuperService.getHeroes().get(Integer.parseInt(heroId)));
        }
        List<Hero> enemiesHeroes = new ArrayList<>();
        for(String heroId : enemies.split(",")){
            if (heroId=="")
                break;
            enemiesHeroes.add(SuperService.getHeroes().get(Integer.parseInt(heroId)));
        }

        Selection selection = new Selection(
                selectionName,
                durationMin,
                durationMax,
                patchMin,
                patchMax,
                dateMin,
                dateMax,
                alliesHeroes,
                enemiesHeroes,
                heroHero
        );

        mov.setViewName("BubbleChart");
        return mov;
    }
    @GetMapping("/BuildChart")
    ModelAndView getBuildBubbleChart(Model model){
        BubbleChart chart =(BubbleChart) model.getAttribute("chart");
        ModelAndView mov = new ModelAndView();
        BubbleData data = null;
        try {
            data = SuperService.getBubbleData(chart);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mov.addObject("chartData", data);
        mov.addObject("yAxis", chart.yAxis);
        mov.addObject("xAxis", chart.xAxis);
        mov.setViewName("BubbleChart");
        return mov;
    }
}
