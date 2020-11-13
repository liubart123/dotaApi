package com.lojka.kurs.model.user;

import org.springframework.stereotype.Component;

@Component
public class User {
    public String login;
    public String password;
    public EUserRoles role = EUserRoles.USER;
    public Integer id=-1;

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public EUserRoles getRole() {
        return role;
    }

    public void setRole(EUserRoles role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
