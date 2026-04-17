package com.url.URL_Shortner.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class AuthRequest implements Serializable {
    private String email;
    private String password;
}