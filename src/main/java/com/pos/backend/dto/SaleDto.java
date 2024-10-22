package com.pos.backend.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleDto {

    private LocalDateTime saleDate;
    private String userName;
}
