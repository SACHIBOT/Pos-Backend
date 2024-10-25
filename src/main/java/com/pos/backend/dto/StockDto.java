package com.pos.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StockDto {
    private Long itemId;
    private int quantity;
    private String unit;
}
