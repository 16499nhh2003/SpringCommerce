package com.project.spring.repositories;

import com.project.spring.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> getCategoriesByName(String name);
    Optional<Category> findCategoriesByNameContainingIgnoreCase(String name);
}
