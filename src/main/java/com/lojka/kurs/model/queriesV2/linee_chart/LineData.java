package com.lojka.kurs.model.queriesV2.linee_chart;

import com.lojka.kurs.model.queriesV2.bar.BarDataSet;

import java.util.ArrayList;
import java.util.List;

public class LineData {
    public String name;
    public List<String> labels = new ArrayList<>();
    public List<LineDataSet> datasets = new ArrayList<>();
}
