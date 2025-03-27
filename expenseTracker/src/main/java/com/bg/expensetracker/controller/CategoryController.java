package com.bg.expensetracker.controller;

import com.bg.expensetracker.model.Category;
import com.bg.expensetracker.repository.CategoryRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CategoryController {
    private CategoryRepo categoryRepo;
    public CategoryController(CategoryRepo categoryRepo) {
        super();
        this.categoryRepo = categoryRepo;
    }

    @GetMapping("/categories")
    Collection<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @GetMapping("/category/{id}")
    ResponseEntity<?> getCategory(@PathVariable Long id) {
        Optional<Category> category = categoryRepo.findById(id);
        return category.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/category")
    ResponseEntity<Category> addCategory(@Validated @RequestBody Category category) throws URISyntaxException {
        Category category1 = categoryRepo.save(category);
        return ResponseEntity.created(new URI("/api/category/" + category1.getId())).body(category1);
    }

    @PutMapping("/category/{id}")
    ResponseEntity<Category> updateCategory(@Validated @RequestBody Category category) throws URISyntaxException {
        Category category1 = categoryRepo.save(category);
        return ResponseEntity.ok(category1);
    }

    @DeleteMapping("/category/{id}")
    ResponseEntity<?> deleteCategory(@PathVariable Long id) {
        categoryRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

