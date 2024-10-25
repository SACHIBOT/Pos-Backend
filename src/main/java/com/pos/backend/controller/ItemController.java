package com.pos.backend.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Item Controller", description = "Manages item creation, retrieval, and deletion for the POS system")
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    StockService stockService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/items")
    @Operation(summary = "Retrieve all items", description = "Returns a list of all available items.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all items"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getAllItems() {
        return ResponseEntity.status(200).body(itemService.getAllItems());
    }

    @GetMapping("/items/{id}")
    @Operation(summary = "Retrieve an item by ID", description = "Fetches an item by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided")
    })
    public ResponseEntity<?> getItemByID(@PathVariable Long id) {
        Item item = itemService.getItemById(id);
        if (item == null) {
            return ResponseEntity.status(404).body("Item not found");
        }
        return ResponseEntity.status(200).body(item);
    }

    @GetMapping("/items/category/{id}")
    @Operation(summary = "Retrieve an items by Category ID", description = "Fetches an items by Category ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Items successfully retrieved"),
            @ApiResponse(responseCode = "404", description = "Item not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> getItemsByCategoryID(@PathVariable Long id) {
        List<Item> items = itemService.getItemsByCategoryId(id);
        return ResponseEntity.status(200).body(items);
    }

    @PostMapping("/manager/items")
    @Operation(summary = "Create a new item", description = "Creates a new item and its associated stock entry.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item successfully created"),
            @ApiResponse(responseCode = "400", description = "Bad request, invalid item data or category ID"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    public ResponseEntity<?> createItem(@RequestBody ItemDto itemDto) {
        if (itemDto.getItemName() == null || itemDto.getItemName().isEmpty()) {
            return ResponseEntity.status(400).body("Please enter a valid item name");
        }
        if (itemDto.getItemCategoryId() == null || itemDto.getItemCategoryId() <= 0) {
            return ResponseEntity.status(400)
                    .body("Invalid category ID provided. Category ID must be a positive number.");
        }

        try {
            Item existingItem = itemService.getItemByName(itemDto.getItemName());
            if (existingItem != null) {
                return ResponseEntity.status(400).body("Item already exists");
            }

            Category category = categoryService.getCategoryById(itemDto.getItemCategoryId());
            if (category == null) {
                return ResponseEntity.status(404).body("Category not found. Please enter a valid Category ID.");
            }

            Item item = new Item();
            item.setItemName(itemDto.getItemName());
            item.setItemCategory(category);
            item.setDescription(itemDto.getDescription());
            item.setPrice(itemDto.getPrice());
            item = itemService.createItem(item);

            Stock stock = new Stock();
            stock.setItem(item);
            stock.setQuantity(0);
            stock = stockService.createStock(stock);
            if (itemDto.getUnit() != null) {
                stock.setUnit(itemDto.getUnit());
            }
            item.setStock(stock);
            return ResponseEntity.status(201).body(item);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PutMapping("/manager/items/{id}")
    @Operation(summary = "Update an existing Item", description = "Updates an existing item based on its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid request, please provide a uniq Item name"),
            @ApiResponse(responseCode = "404", description = "Item not found or category not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestBody ItemDto itemDto) {
        if (itemDto.getItemName() == null || itemDto.getItemName().isEmpty()) {
            return ResponseEntity.status(400).body("Please enter a valid item name");
        }

        Item existingItem = itemService.getItemById(id);
        if (existingItem == null) {
            return ResponseEntity.status(404).body("Item not found");
        }
        Item existingItem2 = itemService.getItemByName(itemDto.getItemName());
        if (existingItem2 != null && existingItem2.getItemId() != id) {
            return ResponseEntity.status(400).body("Item name already exists. Please use uniq item name.");
        }
        Category category = categoryService.getCategoryById(itemDto.getItemCategoryId());
        if (category == null) {
            return ResponseEntity.status(404).body("Category not found. Please enter a valid Category ID.");

        }

        existingItem.setItemName(itemDto.getItemName());
        existingItem.setPrice(itemDto.getPrice());
        existingItem.setDescription(itemDto.getDescription());
        existingItem.setItemCategory(category);

        try {
            Item updatedItem = itemService.updateItem(existingItem);
            return ResponseEntity.status(200).body(updatedItem);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @DeleteMapping("/manager/items/{id}")
    @Operation(summary = "Delete an item by ID", description = "Deletes an item and its associated stock entry by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Item successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Item or stock not found"),
            @ApiResponse(responseCode = "400", description = "Invalid ID provided")
    })
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(400).body("Invalid ID provided. ID must be a positive number.");
        }
        try {
            Stock stock = stockService.getStockByItemId(id);
            if (stock != null) {
                stockService.deleteStock(stock.getStockId());
            }
            itemService.deleteItem(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Something went wrong while deleting item");
        }
    }
}
