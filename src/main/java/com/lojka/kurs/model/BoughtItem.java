package com.lojka.kurs.model;

import java.io.Serializable;

public class BoughtItem implements Serializable {
    public Integer time;
    public String key;
    public Integer itemId;

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
