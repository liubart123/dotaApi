package com.lojka.kurs.model.queriesV2.bubble;

import com.lojka.kurs.model.queriesV2.Selection;

import java.util.ArrayList;
import java.util.List;

//графік з якім працуе superservice і які захоўваецца ў бд
public class BubbleChart {
    public Integer minCountOfMatches = 0;
    public String name;
    public String xAxis, yAxis;
    public Float xScale;
    public List<Selection> selections = new ArrayList<>();
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

    public Float getxScale() {
        return xScale;
    }

    public void setxScale(Float xScale) {
        this.xScale = xScale;
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
                ", xScale=" + xScale;
    }
}
