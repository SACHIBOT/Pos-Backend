package com.pos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.pos.backend.dto.SaleDto;
import com.pos.backend.dto.SaleItemDto;
import com.pos.backend.entity.Sale;
import com.pos.backend.entity.SaleItem;
import com.pos.backend.service.ItemService;
import com.pos.backend.service.SaleItemService;
import com.pos.backend.service.SaleService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@Tag(name = "Sale Controller", description = "Manages sale creation and adding items to sales in the POS system")
public class SaleController {

    @Autowired
    SaleService saleService;

    @Autowired
    SaleItemService saleItemService;

    @Autowired
    ItemService itemService;

    @GetMapping("/sale/{id}")
    @Operation(summary = "Get Sale by ID", description = "Fetches the sale details for the given sale ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getSaleInfo(@PathVariable Long id) {
        Sale sale = saleService.getSaleById(id);
        if (sale == null) {
            return ResponseEntity.status(404).body("Sale not found");
        }
        return ResponseEntity.status(201).body(sale);
    }

    @PostMapping("/sale")
    @Operation(summary = "Create a new Sale", description = "Creates a new sale and returns the created sale entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid sale data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createSale(@RequestBody SaleDto saleDto) {
        Sale sale = new Sale();
        sale.setSaleDate(saleDto.getSaleDate());
        sale.setUserId(saleDto.getUserId());

        try {
            sale = saleService.createSale(sale);
            return ResponseEntity.status(201).body(sale);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }

    @PostMapping("/sale/{id}")
    @Operation(summary = "Add items to a Sale", description = "Adds items to an existing sale and returns the added sale items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Sale item successfully added"),
            @ApiResponse(responseCode = "404", description = "Sale not found"),
            @ApiResponse(responseCode = "400", description = "Invalid item data provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> addSaleItems(@PathVariable Long id, @RequestBody SaleItemDto saleItemDto) {
        Sale sale = saleService.getSaleById(id);
        if (sale == null) {
            return ResponseEntity.status(404).body("Sale not found");
        }

        try {
            SaleItem saleItem = new SaleItem();
            saleItem.setSale(sale);
            saleItem.setPrice(itemService.getItemById(saleItemDto.getItemId()).getPrice());
            saleItem.setQuantity(saleItemDto.getQuantity());
            saleItem.setItem(itemService.getItemById(saleItemDto.getItemId()));

            saleItem = saleService.addSaleItem(saleItem);
            return ResponseEntity.status(201).body(saleItem);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error: " + e.getMessage());
        }
    }
}
