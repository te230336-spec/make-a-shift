package com.example.shift.domain;

public class Staff {

    private Long id;
    private String name;
    private Role role;

    public Staff() {}

    public Staff(Long id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public boolean isLeader() {
        return role == Role.STORE_MANAGER || role == Role.VETERAN;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Role getRole() { return role; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setRole(Role role) { this.role = role; }
}