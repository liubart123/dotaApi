package com.lojka.kurs.model.queriesV2.bar;

import java.util.ArrayList;
import java.util.List;

public class BarData {
    public String name;
    public List<String> labels = new ArrayList<>();
    public List<BarDataSet> datasets = new ArrayList<>();
}
