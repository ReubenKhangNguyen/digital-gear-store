package com.phuckhang.digital_store.catalog.service;

import com.phuckhang.digital_store.catalog.dto.request.category.CategoryRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.category.CategoryResponseDTO;

import java.util.List;

public interface CategoryService {

    // them danh muc
    CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO);

    // lấy cây danh mục
    List<CategoryResponseDTO> getCategoryTree();

    // Lấy 1 danh mục
    CategoryResponseDTO getCategoryById(Long id);

    // cập nhật
    CategoryResponseDTO updateCategory(Long Id, CategoryRequestDTO requestDTO);

    // xóa danh mục (cập nhật status)
    void  deleteCategory(Long Id);
}
