package com.lojka.kurs.model.queriesV2.linee_chart;

import com.lojka.kurs.model.queriesV2.Selection;

import java.util.ArrayList;
import java.util.List;

//графік для атрымання мінімальных і максімальных паказчыкаў выбаркі
public class LineChart {
    public Integer minCountOfMatches = 0;
    public String name;
    public String xAxis, yAxis;
    public Integer countOfLabels = 0;
    public Boolean isDeasc = false;
    public Selection selection;
    public Integer id;

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

    public Integer getCountOfLabels() {
        return countOfLabels;
    }

    public void setCountOfLabels(Integer countOfLabels) {
        this.countOfLabels = countOfLabels;
    }

    public Boolean getDeasc() {
        return isDeasc;
    }

    public void setDeasc(Boolean deasc) {
        isDeasc = deasc;
    }

    public Selection getSelection() {
        return selection;
    }

    public void setSelection(Selection selection) {
        this.selection = selection;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
