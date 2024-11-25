package com.cst8277.usermanagementservice;

import java.util.UUID;
import java.util.List;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private int created;
    private UUID lastVisitId;
    private List<String> roles;

    public User(UUID id, String name, String email, String password, int created, UUID lastVisitId, List<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.created = created;
        this.lastVisitId = lastVisitId;
        this.roles = roles;
    }

    // no roles
    public User(UUID id, String name, String email, String password, int created, UUID lastVisitId) {
        this(id, name, email, password, created, lastVisitId, null);
    }

    // no password
    public User(UUID id, String name, String email, int created, UUID lastVisitId, String roles) {
        this(id, name, email, null, created, lastVisitId, roles != null ? List.of(roles.split(",")) : null);
    }

    // all null
    public User() {
        this(null, null, null, null, 0, null, null);
    }

    //getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public UUID getLastVisitId() {
        return lastVisitId;
    }

    public void setLastVisitId(UUID lastVisitId) {
        this.lastVisitId = lastVisitId;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}
