package com.lojka.kurs.model.queriesV2;

import java.util.ArrayList;
import java.util.List;

public class BubbleChart {
    public String name;
    public String xAxis, yAxis;
    public Float xScale;
    public List<Selection> selections = new ArrayList<>();
}
