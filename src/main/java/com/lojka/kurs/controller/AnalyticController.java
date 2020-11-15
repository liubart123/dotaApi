package com.lojka.kurs.controller;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.queriesV2.bar.BarChart;
import com.lojka.kurs.model.queriesV2.Selection;
import com.lojka.kurs.model.user.CustomUserDetails;
import com.lojka.kurs.model.user.User;
import com.lojka.kurs.service.app.ChartSelectionService;
import com.lojka.kurs.service.super_service.SuperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/analytic")
@RestController
public class AnalyticController {
    @Autowired
    ChartSelectionService service;

    @GetMapping("/CreateSelection")
    ModelAndView getCreateSelection(Model model){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("Analytic/CreateSelection");
        mov.addObject("heroes", SuperService.getHeroes());
        mov.addObject("items", SuperService.getItems());
        mov.addObject("selection", new Selection());
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


//        mov.setViewName("redirect:/analytic/CreateSelection");
        mov.setViewName("/Analytic/CreateSelection");
        mov.addObject("heroes", SuperService.getHeroes());
        mov.addObject("items", SuperService.getItems());
        mov.addObject("selection", new Selection());
        User user  = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        try {
            service.createSelection(user,selection);
            mov.addObject("infoMessage","Selection has been created! ");
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
//        service.createSelection(principal.);
        return mov;
    }


    @GetMapping("/UpdateSelection/{id}")
    ModelAndView getUpdateSelection(Model model,
                                    @PathVariable(value="id") Integer id){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("Analytic/UpdateSelection");
        mov.addObject("heroes", SuperService.getHeroes());
        mov.addObject("items", SuperService.getItems());
        try {

            Selection selection = service.getSelection(id);
            mov.addObject("selection", selection);
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage","error with db");

        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        return mov;
    }
    @PostMapping("/UpdateSelection")
    ModelAndView postUpdateSelection(Model model, Selection selection,
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


//        mov.setViewName("redirect:/analytic/CreateSelection");
        mov.setViewName("/Analytic/CreateSelection");
        mov.addObject("heroes", SuperService.getHeroes());
        mov.addObject("items", SuperService.getItems());
        mov.addObject("selection", selection);
        User user  = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        try {
            service.updateSelection(user,selection);
            mov.addObject("infoMessage","Selection has been updated! ");
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
//        service.createSelection(principal.);
        return mov;
    }

    @GetMapping("/DeleteSelection/{id}")
    ModelAndView getDeleteSelection(Model model,
                                    @PathVariable(value="id") Integer id){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("redirect:/analytic/Selections");
        try {
            service.deleteSelection(id);
            mov.addObject("infoMessage","selection was deleted");
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage","error with db");

        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        return mov;
    }
    @GetMapping("/Selections")
    ModelAndView getSelections(Model model){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("Analytic/Selections");

        User user  = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        try {
            mov.addObject("selections",service.getSelections(user));
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage","Error with query");
        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        return mov;
    }



    @GetMapping("/CreateBarChart")
    ModelAndView getCreateBarChart(Model model){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("Analytic/CreateSelection");
        mov.addObject("heroes", SuperService.getHeroes());
        mov.addObject("items", SuperService.getItems());
        mov.addObject("chart", new BarChart());
        User user  = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        try {
            mov.addObject("selections",service.getSelections(user));
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage","Error with query");
        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }

        mov.setViewName("Analytic/Charts/CreateBarChart");
        return mov;
    }
    @PostMapping("/CreateBarChart")
    ModelAndView postCreateBarChart(Model model, BarChart chart,
                                    @RequestParam String heroString,
                                    @RequestParam String itemsString,
                                    @RequestParam String selectionsIds){
        ModelAndView mov = new ModelAndView();
        try{
            List<String> xLabels = new ArrayList<>();
            String xLabelString;
            if (chart.xAxis.equals("allies") || chart.xAxis.equals("enemies")){
                xLabelString = heroString;
            }else {
                xLabelString = itemsString;
            }
            for(String xLabel : xLabelString.split(",")){
                if (xLabel=="")
                    break;
                xLabels.add(xLabel);
            }
            chart.xLabels = xLabels;


            for(String selection : selectionsIds.split(",")){
                if (selection=="")
                    break;
                chart.selections.add(service.getSelection(Integer.parseInt(selection)));
            }

            User user  = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            service.createBarChart(user,chart);


            mov.setViewName("Analytic/CreateSelection");
            mov.addObject("heroes", SuperService.getHeroes());
            mov.addObject("items", SuperService.getItems());
            mov.addObject("chart", chart);

            mov.addObject("infoMessage","chart was created");
            mov.setViewName("redirect:/analytic/BarCharts");
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        return mov;
    }
    @GetMapping("/BarCharts")
    ModelAndView getCharts(Model model){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("Analytic/Charts/BarCharts");

        User user  = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();

        try {
            mov.addObject("barCharts",service.getBarCharts(user));
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage","Error with query");
        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        return mov;
    }

    @GetMapping("/UpdateBarChart/{id}")
    ModelAndView getUpdateBarChart(Model model,@PathVariable(value="id") Integer id){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("Analytic/Charts/UpdateBarChart");
        mov.addObject("heroes", SuperService.getHeroes());
        mov.addObject("items", SuperService.getItems());
        try {
            BarChart chart = service.getBarChart(id);
            mov.addObject("chart", chart);
            User user  = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            mov.addObject("selections",service.getSelections(user));
            mov.addObject("chartSelections",chart.getSelections());
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage","Error with query");
        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }

        return mov;
    }
    @PostMapping("/UpdateBarChart")
    ModelAndView postUpdateBarChart(Model model, BarChart chart,
                                    @RequestParam String heroString,
                                    @RequestParam String itemsString,
                                    @RequestParam String selectionsIds){
        ModelAndView mov = new ModelAndView();
        try{
            List<String> xLabels = new ArrayList<>();
            String xLabelString;
            if (chart.xAxis.equals("allies") || chart.xAxis.equals("enemies")){
                xLabelString = heroString;
            }else {
                xLabelString = itemsString;
            }
            for(String xLabel : xLabelString.split(",")){
                if (xLabel=="")
                    break;
                xLabels.add(xLabel);
            }
            chart.xLabels = xLabels;


            for(String selection : selectionsIds.split(",")){
                if (selection=="")
                    break;
                chart.selections.add(service.getSelection(Integer.parseInt(selection)));
            }

            mov.setViewName("Analytic/CreateSelection");
            mov.addObject("heroes", SuperService.getHeroes());
            mov.addObject("items", SuperService.getItems());
            mov.addObject("chart", chart);

            User user  = ((CustomUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            service.updateBarChart(chart);


            mov.addObject("infoMessage","chart was updated");
            mov.setViewName("redirect:/analytic/BarCharts");
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        return mov;
    }

    @GetMapping("/DeleteBarChart/{id}")
    ModelAndView getDeleteBarChart(Model model,@PathVariable(value="id") Integer id){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("redirect:/analytic/BarCharts");
        try {
            service.deleteBarChart(id);
            mov.addObject("infoMessage","chart was deleted");
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage","error with db");

        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        return mov;
    }
}
