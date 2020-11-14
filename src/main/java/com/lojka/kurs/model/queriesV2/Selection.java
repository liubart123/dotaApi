package com.lojka.kurs.model.queriesV2;

import com.lojka.kurs.model.Hero;
import com.lojka.kurs.model.Item;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
//параметры выбаркі для будучага графіка ці дыяграмы
public class Selection {
    public Integer id;
    public String selectionName = "some selection";
    public Integer durationMin = 0;
    public Integer durationMax = 999;
    public Integer patchMin = 0;
    public Integer patchMax = 999;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date dateMin = new Date(1558708419000l);
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date dateMax = new Date(1621866819000l);
    public List<Hero> allies = new ArrayList<>();
    public List<Hero> enemies = new ArrayList<>();
    public List<Item> items = new ArrayList<>();
    public Hero hero;

    public Selection(String selectionName, Integer durationMin, Integer durationMax, Integer patchMin, Integer patchMax, Date dateMin, Date dateMax, List<Hero> allies, List<Hero> enemies, Hero hero, List<Item> items) {
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
        this.items = items;
    }

    public Selection() {
    }

    public String getSelectionName() {
        return selectionName;
    }

    public void setSelectionName(String selectionName) {
        this.selectionName = selectionName;
    }

    public Integer getDurationMin() {
        return durationMin;
    }

    public void setDurationMin(Integer durationMin) {
        this.durationMin = durationMin;
    }

    public Integer getDurationMax() {
        return durationMax;
    }

    public void setDurationMax(Integer durationMax) {
        this.durationMax = durationMax;
    }

    public Integer getPatchMin() {
        return patchMin;
    }

    public void setPatchMin(Integer patchMin) {
        this.patchMin = patchMin;
    }

    public Integer getPatchMax() {
        return patchMax;
    }

    public void setPatchMax(Integer patchMax) {
        this.patchMax = patchMax;
    }

    public Date getDateMin() {
        return dateMin;
    }

    public void setDateMin(Date dateMin) {
        this.dateMin = dateMin;
    }

    public Date getDateMax() {
        return dateMax;
    }

    public void setDateMax(Date dateMax) {
        this.dateMax = dateMax;
    }

    public List<Hero> getAllies() {
        return allies;
    }

    public void setAllies(List<Hero> allies) {
        this.allies = allies;
    }

    public List<Hero> getEnemies() {
        return enemies;
    }

    public void setEnemies(List<Hero> enemies) {
        this.enemies = enemies;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String parametersToString() {
        return
                "durationMin=" + durationMin +
                ", durationMax=" + durationMax +
                ", patchMin=" + patchMin +
                ", patchMax=" + patchMax +
                ", dateMin=" + dateMin +
                ", dateMax=" + dateMax;
    }
}
