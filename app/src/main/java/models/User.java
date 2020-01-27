package models;

import enums.Role;

public class User {

    private String email;
    private String password;
    private Role userRole;

    public User(String email, String password, Role userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getUserRole() {
        return userRole;
    }
}
