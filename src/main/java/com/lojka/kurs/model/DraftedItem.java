package com.lojka.kurs.model;

import java.io.Serializable;

public class DraftedItem implements Serializable {

    public Byte order;
    public Boolean pick;
    public Byte active_team;
    public Integer hero_id;
    //public Integer? player_slot;
    public Integer extra_time;
    public Integer total_time_taken;
}
