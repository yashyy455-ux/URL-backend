package com.url.URL_Shortner.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class UserResponse implements Serializable {
    private Long id;
    private String email;
    private String role;
}