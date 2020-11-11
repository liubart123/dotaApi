package com.lojka.kurs.controller;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;
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
    ModelAndView getCreateSelection(Model model){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("Analytic/CreateSelection");
        mov.addObject("heroes", SuperService.getHeroes());
        mov.addObject("items", SuperService.getItems());
        mov.addObject("selection", new Selection());
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

    @PostMapping("/CreateSelection")
    ModelAndView postCreateSelection(Model model, Selection selection,
                                     @RequestParam String alliesString,
                                     @RequestParam String enemiesString,
                                     @RequestParam String heroString,
                                     @RequestParam String itemsString
                                     ){
        ModelAndView mov = new ModelAndView();

        Hero heroHero = SuperService.getHeroes().get(Integer.parseInt(heroString));
        List<Hero> alliesHeroes = new ArrayList<>();
        for(String heroId : alliesString.split(",")){
            if (heroId=="")
                break;
            alliesHeroes.add(SuperService.getHeroes().get(Integer.parseInt(heroId)));
        }
        List<Hero> enemiesHeroes = new ArrayList<>();
        for(String heroId : enemiesString.split(",")){
            if (heroId=="")
                break;
            enemiesHeroes.add(SuperService.getHeroes().get(Integer.parseInt(heroId)));
        }
        List<Item> boughtItems = new ArrayList<>();
        for(String itemId : itemsString.split(",")){
            if (itemId=="")
                break;
            boughtItems.add(SuperService.getItems().get(Integer.parseInt(itemId)));
        }

        selection.hero = heroHero;
        selection.allies = alliesHeroes;
        selection.enemies = enemiesHeroes;
        selection.items = boughtItems;

        mov.setViewName("Analytic/CreateSelection");
        return mov;
    }
}
