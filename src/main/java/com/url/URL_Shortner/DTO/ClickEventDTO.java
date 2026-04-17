package com.url.URL_Shortner.DTO;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class ClickEventDTO implements Serializable {
    private LocalDate clickDate;
    private Long count;
}