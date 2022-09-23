package com.stuthemp.springboot.soap.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Дата класс для сущности User
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    private String login;
    private String name;
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String login, String name, String password, Set<Role> roles) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.roles = roles;
    }

    public void addRole(Role role) {
        roles.add(role);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

}
