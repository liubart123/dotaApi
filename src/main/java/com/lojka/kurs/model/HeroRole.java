package com.lojka.kurs.model;

import java.io.Serializable;
import java.util.Objects;

public class HeroRole implements Serializable {
    String roleName;

    public HeroRole(String roleName, Integer id) {
        this.roleName = roleName;
        this.id = id;
    }

    Integer id = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public HeroRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeroRole)) return false;
        HeroRole heroRole = (HeroRole) o;
        return getRoleName().equals(heroRole.getRoleName()) &&
                Objects.equals(getId(), heroRole.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRoleName(), getId());
    }
}
