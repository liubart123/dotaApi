package com.lojka.kurs.controller.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.exception.UserAuthoException;
import com.lojka.kurs.model.queriesV2.bar.BarChart;
import com.lojka.kurs.model.queriesV2.bar.BarData;
import com.lojka.kurs.model.queriesV2.bubble.BubbleChart;
import com.lojka.kurs.model.queriesV2.bubble.BubbleData;
import com.lojka.kurs.model.queriesV2.Selection;
import com.lojka.kurs.model.queriesV2.linee_chart.LineChart;
import com.lojka.kurs.model.queriesV2.linee_chart.LineData;
import com.lojka.kurs.model.user.EUserRoles;
import com.lojka.kurs.model.user.User;
import com.lojka.kurs.service.app.UserDetailsServiceImpl;
import com.lojka.kurs.service.super_service.SuperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@RestController
public class UserController {
    @Autowired
    UserDetailsServiceImpl userService;

    @GetMapping("/login")
    public ModelAndView getLogin(Model model, Principal principal){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("login");
        Object pr = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object aut = SecurityContextHolder.getContext().getAuthentication();
        return mov;
    }
    @PostMapping("/login")
    public ModelAndView postLogin(Model model, User user){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("login");
        return mov;
    }

    @GetMapping("/signUp")
    public ModelAndView getSignUp(Model model, Principal principal){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("signUp");
        mov.addObject("user",new User());
        return mov;
    }
    @PostMapping("/signUp")
    public ModelAndView postSignUp(Model model, Principal principal, User user){
        ModelAndView mov = new ModelAndView();
        mov.setViewName("signUp");
        mov.addObject("user",new User());
        try {
            userService.signUpUser(user);
            mov.addObject("infoMessage", "you have been signed up");
        } catch (DbAccessException | UserAuthoException e) {
            e.printStackTrace();
            mov.setViewName("signUp");
            mov.addObject("errorMessage",e.getMessage());
        }
        return mov;
    }

}
