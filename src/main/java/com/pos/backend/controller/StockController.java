package com.pos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.pos.backend.dto.StockDto;
import com.pos.backend.entity.Stock;
import com.pos.backend.service.StockService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class StockController {
    @Autowired
    StockService stockService;

    @PutMapping("/stocks")
    public ResponseEntity<?> updateStock(@RequestBody StockDto stockDto) {
        if (stockDto.getQuantity() <= 0) {
            return ResponseEntity.status(400).body("Please enter a positive quantity");
        }
        if (stockDto.getItemId() == null || stockDto.getItemId() <= 0) {
            return ResponseEntity.status(400).body("Invalid  item ID provided. Item ID must be a positive number.");
        }

        try {
            Stock stock = stockService.getStockByItemId(stockDto.getItemId());
            if (stock == null) {
                return ResponseEntity.status(404).body("Item stock not found");
            }
            int availableQty = stock.getQuantity();
            int newQty = availableQty + stockDto.getQuantity();
            if (newQty < 0) {
                return ResponseEntity.status(400).body("Quantity cannot be negative");
            }
            stock.setQuantity(newQty);
            Stock updatedStock = stockService.updateStock(stock.getStockId(), stock);
            return ResponseEntity.status(201).body(updatedStock);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

}
