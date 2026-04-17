package com.url.URL_Shortner.DTO;

import com.url.URL_Shortner.Entity.Role;
import lombok.Data;

import java.io.Serializable;

@Data
public class RegisterUserRequest implements Serializable {
    private String email;
    private String username;
    private String password;
    private Role role = Role.USER;
}