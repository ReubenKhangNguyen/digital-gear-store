package com.phuckhang.digital_store.catalog.repository;

import com.phuckhang.digital_store.catalog.entity.Category;
import com.phuckhang.digital_store.catalog.enums.CategoryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);

    List<Category> findAllByCategoryStatus(CategoryStatus status);
}
