package com.pos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.pos.backend.dto.StockDto;
import com.pos.backend.entity.Stock;
import com.pos.backend.service.StockService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Stock Controller", description = "Manages stock retrieval and updates for items in the POS system")
public class StockController {

    @Autowired
    StockService stockService;

    @GetMapping("/stocks/{id}")
    @Operation(summary = "Get stock by ID", description = "Retrieves the stock details for a specific item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved stock details"),
            @ApiResponse(responseCode = "404", description = "Stock not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getStockById(@PathVariable Long id) {
        Stock stock = stockService.getStockById(id);
        if (stock == null) {
            return ResponseEntity.status(404).body("Stock not found");
        }
        return ResponseEntity.status(200).body(stock);
    }

    @PutMapping("/manager/stocks")
    @Operation(summary = "Update stock", description = "Allows a manager to update the quantity of stock for a specific item.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Stock successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request, please check the input values"),
            @ApiResponse(responseCode = "404", description = "Item stock not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> updateStock(@RequestBody StockDto stockDto) {
        if (stockDto.getQuantity() <= 0) {
            return ResponseEntity.status(400).body("Please enter a positive quantity");
        }
        if (stockDto.getItemId() == null || stockDto.getItemId() <= 0) {
            return ResponseEntity.status(400).body("Invalid item ID provided. Item ID must be a positive number.");
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
            return ResponseEntity.status(200).body(updatedStock);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
