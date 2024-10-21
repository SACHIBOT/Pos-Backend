package com.pos.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pos.backend.dto.CategoryDto;
import com.pos.backend.entity.Category;
import com.pos.backend.service.CategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Category Controller", description = "Manages category creation and retrieval for the POS system")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories")
    @Operation(summary = "Create a new Category", description = "Creates a new category and returns the created entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid request, please provide a valid category name or the category already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<?> createCategory(@RequestBody CategoryDto categoryDto) {
        if (categoryDto.getCategoryName() == null || categoryDto.getCategoryName().isEmpty()) {
            return ResponseEntity.status(400).body("Please enter a valid category name");
        }

        Category existingCategory = categoryService.getCategoryByCategoryName(categoryDto.getCategoryName());
        if (existingCategory != null) {
            return ResponseEntity.status(400).body("Category already exists");
        }

        Category category = new Category();
        category.setCategoryName(categoryDto.getCategoryName());
        category.setDescription(categoryDto.getDescription());

        try {
            Category createdCategory = categoryService.createCategory(category);
            return ResponseEntity.status(201).body(createdCategory);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @GetMapping("/categories")
    @Operation(summary = "Retrieve all Categories", description = "Returns a list of all available categories.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved categories"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.status(200).body(categories);
    }
}
