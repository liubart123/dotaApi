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
}
