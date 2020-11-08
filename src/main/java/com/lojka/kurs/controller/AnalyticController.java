package com.lojka.kurs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.queries.BubbleData;
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
    @GetMapping("/chartSettings")
    ModelAndView getChartSettingsWindow(Model model){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("Analytic/ChartSetting");
        mov.addObject("heroes", SuperService.getHeroes());
        return mov;
    }
    @PostMapping("/chart")
    ModelAndView postBuildChart(Model model,
                                @RequestParam Integer durationMin,
                                @RequestParam Integer durationMax,
                                @RequestParam Integer patchMin,
                                @RequestParam Integer patchMax,
                                @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") Date dateMin,
                                @RequestParam @DateTimeFormat(pattern = "dd.MM.yyyy") Date dateMax,
                                @RequestParam String yAxis,
                                @RequestParam String xAxis,
                                @RequestParam String allies,
                                @RequestParam String enemies,
                                @RequestParam String hero
                                ){
        ModelAndView mov = new ModelAndView();

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

        BubbleData data = SuperService.GetChart(
                durationMin,
                durationMax,
                patchMin,
                patchMax,
                dateMin,
                dateMax,
                yAxis,
                xAxis,
                alliesHeroes,
                enemiesHeroes,
                SuperService.getHeroes().get(Integer.parseInt(hero))
        );
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(data);
            mov.addObject("chartData", json );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (data!=null){
        }

        mov.setViewName("Analytic/Chart");
        return mov;
    }
}
