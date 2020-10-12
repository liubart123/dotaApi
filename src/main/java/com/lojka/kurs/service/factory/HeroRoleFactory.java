package com.lojka.kurs.service.factory;

import com.lojka.kurs.model.HeroRole;

import java.util.ArrayList;

public class HeroRoleFactory {
    static ArrayList<HeroRole> roles = new ArrayList<>();
    static Integer id=0;


    public static ArrayList<HeroRole> getRoles() {
        return roles;
    }


    public static HeroRole createRole(String roleName){
        for (HeroRole role:
             roles) {
            if (role.getRoleName().equals(roleName)){
                return role;
            }
        }
        id++;
        HeroRole result = new HeroRole(roleName, id);
        roles.add(result);
        return  result;
    }
}
