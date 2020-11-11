package com.lojka.kurs.model.queriesV2.linee_chart;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineDataSet {

    public List<Float> data = new ArrayList<>();
    @JsonIgnore
    public Map<String, Float> mapData = new HashMap<>();
    public String label;

    public LineDataSet(String label) {
        this.label = label;
    }

    public void createDataFromMap(List<String> labels){
        for (String label : labels ){
            if (mapData.get(label)!=null){
                data.add(mapData.get(label));
            } else {
                data.add(0f);
            }
        }
    }
}
