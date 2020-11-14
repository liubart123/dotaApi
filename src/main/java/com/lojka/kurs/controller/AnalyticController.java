package com.lojka.kurs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;
import com.lojka.kurs.model.queriesV2.bubble.BubbleChart;
import com.lojka.kurs.model.queriesV2.bubble.BubbleData;
import com.lojka.kurs.model.queriesV2.Selection;
import com.lojka.kurs.model.user.CustomUserDetails;
import com.lojka.kurs.model.user.User;
import com.lojka.kurs.service.app.ChartSelectionService;
import com.lojka.kurs.service.super_service.SuperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
            //mov.addObject("selection", selection);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(selection);
//            mov.addObject("selectionJson", json );
        } catch (SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage","error with db");

        } catch (DbAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        } catch (JsonProcessingException e) {
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
}
