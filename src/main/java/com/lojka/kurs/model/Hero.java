package com.lojka.kurs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Hero implements Serializable, Comparable<Hero> {
    Integer id;
    String name;
    String description;
    String img;
    String icon;
    List<HeroRole> roles = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Hero() {
    }

    public List<HeroRole> getRoles() {
        return roles;
    }

    public void setRoles(List<HeroRole> roles) {
        this.roles = roles;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public int compareTo(Hero o) {
        return name.compareTo(o.name);
    }
}
