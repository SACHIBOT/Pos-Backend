package com.pos.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto {
    private String itemName;
    private String description;
    private double price;
    private Long itemCategoryId;
}
