package com.sliit.se2030.mall.catalog.repository;

import com.sliit.se2030.mall.catalog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);
}
