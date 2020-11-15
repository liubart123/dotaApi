package com.lojka.kurs.model.queriesV2.bar;


import com.lojka.kurs.model.queriesV2.Selection;

import java.util.ArrayList;
import java.util.List;

public class BarChart {
    public Integer id;
    public Integer minCountOfMatches = 0;
    public String name;
    public String xAxis, yAxis;
    public List<String> xLabels = new ArrayList<>();
    public List<Selection> selections = new ArrayList<>();

    public Integer getMinCountOfMatches() {
        return minCountOfMatches;
    }

    public void setMinCountOfMatches(Integer minCountOfMatches) {
        this.minCountOfMatches = minCountOfMatches;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getxAxis() {
        return xAxis;
    }

    public void setxAxis(String xAxis) {
        this.xAxis = xAxis;
    }

    public String getyAxis() {
        return yAxis;
    }

    public void setyAxis(String yAxis) {
        this.yAxis = yAxis;
    }

    public List<String> getxLabels() {
        return xLabels;
    }

    public void setxLabels(List<String> xLabels) {
        this.xLabels = xLabels;
    }

    public List<Selection> getSelections() {
        return selections;
    }

    public void setSelections(List<Selection> selections) {
        this.selections = selections;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String parametersToString() {
        return
                "minCountOfMatches=" + minCountOfMatches +
                ", name='" + name + '\'' +
                ", xAxis='" + xAxis + '\'' +
                ", yAxis='" + yAxis + '\'' +
                ", xLabels=" + xLabels;
    }
}
