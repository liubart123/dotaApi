package com.lojka.kurs.model.queriesV2.bubble;

import java.util.ArrayList;
import java.util.List;

public class BubbleDataSet {
    public String label;
    public List<BubbleCoord> data= new ArrayList<>();

    public BubbleDataSet() {
    }

    public BubbleDataSet(String label) {
        this.label = label;
    }
    //    public Color borderColor = new Color(12,232,42,1);
//    public Color backgroundColor = new Color(112,132,142,1);
}
