package com.lojka.kurs.controller;

import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.DbConnectionClosedException;
import com.lojka.kurs.exception.DotaDataAccessException;
import com.lojka.kurs.model.user.EUserRoles;
import com.lojka.kurs.model.user.User;
import com.lojka.kurs.service.app.ChartSelectionService;
import com.lojka.kurs.service.super_service.EFilterForMatchInserting;
import com.lojka.kurs.service.super_service.SuperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

@RequestMapping("/admin")
@RestController
public class AdminController {
    @GetMapping("/functions")
    ModelAndView getAdminFunctions(){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("/AdminFunctions");
        return mov;
    }
    @GetMapping("/loadMatches")
    ModelAndView loadRecentMatches(){
        ModelAndView mov = new ModelAndView();
        try {
            SuperService.insertMatches(EFilterForMatchInserting.professionals);
            mov.addObject("infoMessage","matches loaded!");
        } catch (DbAccessException | DbConnectionClosedException | DotaDataAccessException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        mov.setViewName("/AdminFunctions");
        return mov;
    }
    @GetMapping("/updateDotaInfo")
    ModelAndView updateDotaInfo(){
        ModelAndView mov = new ModelAndView();
        try {
            SuperService.updateDotaInfoFromApi();
            mov.addObject("infoMessage","info updated");
        } catch (DbAccessException | DotaDataAccessException | DbConnectionClosedException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        mov.setViewName("/AdminFunctions");
        return mov;
    }
    @GetMapping("/createNewAdmin")
    ModelAndView createNewAdmin(){
        ModelAndView mov = new ModelAndView();
        User user = new User();
        user.setRole(EUserRoles.ADMIN);
        mov.addObject("user", user);
        mov.setViewName("/adminSignUp");
        return mov;
    }
    @GetMapping("/clearTables")
    ModelAndView clearTables(){
        ModelAndView mov = new ModelAndView();
        try {
            SuperService.clearTablesFromDb();
            mov.addObject("infoMessage","info updated");
        } catch (DbAccessException | DbConnectionClosedException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        mov.setViewName("/AdminFunctions");
        return mov;
    }

    @Autowired
    ChartSelectionService appService;
    @GetMapping(value="/importDb")
    ModelAndView importDb(){
        ModelAndView mov = new ModelAndView();
        try {
            appService.importApp();
            mov.addObject("infoMessage","db imported");
        } catch (DbAccessException | SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        mov.setViewName("/AdminFunctions");
        return mov;
    }
    @GetMapping(value="/exportDb")
    ModelAndView exportDb(){
        ModelAndView mov = new ModelAndView();
        try {
            appService.exportApp();
            mov.addObject("infoMessage","db exported");
        } catch (DbAccessException | SQLException e) {
            e.printStackTrace();
            mov.addObject("errorMessage",e.getMessage());
        }
        mov.setViewName("/AdminFunctions");
        return mov;
    }
}
