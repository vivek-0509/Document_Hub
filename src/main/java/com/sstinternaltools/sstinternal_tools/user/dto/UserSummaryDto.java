package com.sstinternaltools.sstinternal_tools.user.dto;

import com.sstinternaltools.sstinternal_tools.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class UserSummaryDto {

    @NotBlank(message = "Name cannot be blank")
    private String username;
    @NotBlank(message = "Email cannot be blank")
    private String email;
    @NotEmpty(message = "Roles cannot be null")
    private List<Role> userRoles;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Role> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }
}