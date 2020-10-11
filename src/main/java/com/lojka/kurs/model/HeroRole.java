package com.lojka.kurs.model;

import java.io.Serializable;

public class HeroRole implements Serializable {
    String roleName;

    public HeroRole(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
