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
}
