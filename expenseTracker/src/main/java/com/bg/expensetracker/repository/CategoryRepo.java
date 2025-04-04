package com.bg.expensetracker.repository;

import com.bg.expensetracker.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo  extends JpaRepository<Category, Long> {
    Category findByName(String name);
}
