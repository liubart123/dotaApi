package com.lojka.kurs.model.queriesV2;

import com.lojka.kurs.model.Hero;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
//параметры выбаркі для будучага графіка ці дыяграмы
public class Selection {
    public String selectionName = "some selection";
    public Integer durationMin = 0;
    public Integer durationMax = 999;
    public Integer patchMin = 0;
    public Integer patchMax = 999;
    public Date dateMin = new Date(0l);
    public Date dateMax = new Date(3334941760978l);
    public List<Hero> allies = new ArrayList<>();
    public List<Hero> enemies = new ArrayList<>();
    public Hero hero;

    public Selection(String selectionName, Integer durationMin, Integer durationMax, Integer patchMin, Integer patchMax, Date dateMin, Date dateMax, List<Hero> allies, List<Hero> enemies, Hero hero) {
        if (selectionName!=null) this.selectionName = selectionName;
        if (durationMin!=null) this.durationMin = durationMin;
        if (durationMax!=null) this.durationMax = durationMax;
        if (patchMin!=null) this.patchMin = patchMin;
        if (patchMax!=null) this.patchMax = patchMax;
        if (dateMin!=null) this.dateMin = dateMin;
        if (dateMax!=null) this.dateMax = dateMax;
        this.allies = allies;
        this.enemies = enemies;
        this.hero = hero;
    }

    public Selection() {
    }

}
