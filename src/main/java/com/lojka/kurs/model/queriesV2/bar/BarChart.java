package com.lojka.kurs.model.queriesV2.bar;


import com.lojka.kurs.model.queriesV2.Selection;

import java.util.ArrayList;
import java.util.List;

public class BarChart {
    public Integer minCountOfMatches = 0;
    public String name;
    public String xAxis, yAxis;
    public List<String> xLabels = new ArrayList<>();
    public List<Selection> selections = new ArrayList<>();
}
