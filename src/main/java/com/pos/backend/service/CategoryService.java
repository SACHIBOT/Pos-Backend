package com.pos.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.pos.backend.entity.Category;

@Service
public interface CategoryService {

    Category createCategory(Category category);

    List<Category> getAllCategories();

    Category getCategoryById(Long id);

    Category getCategoryByCategoryName(String categryName);

    Category updateCategory(Category category);

    void deleteCategory(Long id);
}
