package com.lojka.kurs.model;

public class Item {
    Integer id;
    String description;
    String name;

    public Item(Integer id, String description, String name) {
        this.id = id;
        this.description = description;
        this.name = name;
    }

    public Item() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
