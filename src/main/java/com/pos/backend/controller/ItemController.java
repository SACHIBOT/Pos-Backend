package com.pos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.pos.backend.dto.ItemDto;
import com.pos.backend.entity.Category;
import com.pos.backend.entity.Item;
import com.pos.backend.entity.Stock;
import com.pos.backend.service.CategoryService;
import com.pos.backend.service.ItemService;
import com.pos.backend.service.StockService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class ItemController {
    @Autowired
    ItemService itemService;
    @Autowired
    StockService stockService;
    @Autowired
    CategoryService categoryService;

    @PostMapping("/items")
    public ResponseEntity<?> createItem(@RequestBody ItemDto itemDto) {
        if (itemDto.getItemName() == null || itemDto.getItemName().isEmpty()) {
            return ResponseEntity.status(400).body("Please enter a valid category name");
        }
        if (itemDto.getItemCategoryId() == null || itemDto.getItemCategoryId() <= 0) {
            return ResponseEntity.status(400)
                    .body("Invalid  category ID provided. Category ID must be a positive number.");
        }

        try {
            Stock stock = new Stock();
            Item item = new Item();

            Category category = categoryService.getCategoryById(itemDto.getItemCategoryId());

            if (category == null) {
                return ResponseEntity.status(404).body("Category not found. Please enter valid Category ID.");
            }
            item.setItemName(itemDto.getItemName());
            item.setItemCategory(category);
            item.setDescription(itemDto.getDescription());
            item.setPrice(itemDto.getPrice());
            item = itemService.createItem(item);

            stock.setItem(item);
            stock.setQuantity(0);
            stockService.createStock(stock);

            stock = stockService.getStockByItemId(item.getItemId());
            item.setStock(stock);

            return ResponseEntity.status(201).body(item);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
