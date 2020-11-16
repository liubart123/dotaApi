package com.lojka.kurs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lojka.kurs.exception.DbAccessException;
import com.lojka.kurs.model.queriesV2.bar.BarChart;
import com.lojka.kurs.model.queriesV2.bar.BarData;
import com.lojka.kurs.model.queriesV2.bubble.BubbleChart;
import com.lojka.kurs.model.queriesV2.bubble.BubbleData;
import com.lojka.kurs.model.queriesV2.Selection;
import com.lojka.kurs.model.queriesV2.linee_chart.LineChart;
import com.lojka.kurs.model.queriesV2.linee_chart.LineData;
import com.lojka.kurs.model.user.CustomUserDetails;
import com.lojka.kurs.model.user.User;
import com.lojka.kurs.service.app.ChartSelectionService;
import com.lojka.kurs.service.super_service.SuperService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;

@Slf4j
@RequestMapping("/analytic")
@RestController
public class BuildChartsController {
    @Autowired
    ChartSelectionService service;

    @GetMapping(value = "/BuildBubbleChart/{id}")
    public ModelAndView getTest(@PathVariable(value="id") Integer id) throws ParseException {
        ModelAndView mov = new ModelAndView();

        try {
            BubbleChart chart = service.getBubbleChart(id);
            BubbleData data = null;
            data = SuperService.getBubbleData(chart);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(data);
            mov.addObject("chartData", json );
            mov.addObject("yAxis", chart.yAxis);
            mov.addObject("xAxis", chart.xAxis);
            mov.setViewName("Analytic/BubbleChart");
            return mov;
        } catch (Exception e) {
            e.printStackTrace();
            mov.setViewName("Analytic/Charts/BubbleCharts");
            User user  = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            try {
                mov.addObject("barCharts",service.getBubbleCharts(user));
            } catch (SQLException | DbAccessException ex) {
                ex.printStackTrace();
            }
            mov.addObject("errorMessage",e.getMessage());
            return mov;
        }

    }


    @GetMapping(value = "/BuildBarChart/{id}")
    public ModelAndView getTestBar(@PathVariable(value="id") Integer id){
        ModelAndView mov = new ModelAndView();


        try {
            BarChart chart = service.getBarChart(id);
            BarData data = null;
            data = SuperService.getBarData(chart);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(data);
            mov.addObject("chartData", json );
            mov.addObject("yAxis", chart.yAxis);
            mov.addObject("xAxis", chart.xAxis);
            mov.setViewName("Analytic/BarChart");
        } catch (Exception e) {
            e.printStackTrace();
            mov.setViewName("Analytic/Charts/BarCharts");
            User user  = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            try {
                mov.addObject("bubbleCharts",service.getBubbleCharts(user));
            } catch (SQLException | DbAccessException ex) {
                ex.printStackTrace();
            }
            mov.addObject("errorMessage",e.getMessage());
            return mov;
        }

        return mov;
    }


    @GetMapping(value = "/BuildLineChart/{id}")
    public ModelAndView getTestLine(@PathVariable(value="id") Integer id){
        ModelAndView mov = new ModelAndView();
        try {
            LineChart chart = service.getLineChart(id);
            LineData data = null;
            data = SuperService.getLineData(chart);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(data);
            mov.addObject("chartData", json );
            mov.addObject("yAxis", chart.yAxis);
            mov.addObject("y2Axis", "count of matches");
            mov.addObject("xAxis", chart.xAxis);
            mov.setViewName("Analytic/LineChart");
        } catch (Exception e) {
            e.printStackTrace();
            mov.setViewName("Analytic/Charts/LineCharts");
            User user  = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            try {
                mov.addObject("lineCharts",service.getBubbleCharts(user));
            } catch (SQLException | DbAccessException ex) {
                ex.printStackTrace();
            }
            mov.addObject("errorMessage",e.getMessage());
            return mov;
        }

        return mov;
    }

}
