package com.url.URL_Shortner.DTO;

import com.url.URL_Shortner.Entity.Role;
import lombok.Data;

@Data
public class RegisterUserRequest {
    private String email;
    private String username;
    private String password;
    private Role role = Role.USER;
}