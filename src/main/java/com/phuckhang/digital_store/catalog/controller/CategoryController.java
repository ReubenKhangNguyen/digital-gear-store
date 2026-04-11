package com.phuckhang.digital_store.catalog.controller;

import com.phuckhang.digital_store.catalog.dto.request.category.CategoryRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.category.CategoryResponseDTO;
import com.phuckhang.digital_store.catalog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping ("/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryController {

    CategoryService categoryService;

    @GetMapping("/tree")
    public ResponseEntity<List<CategoryResponseDTO>> getCategoryTree(){

        List<CategoryResponseDTO> categoryResponseDTOList = categoryService.getCategoryTree();

        return ResponseEntity.ok(categoryResponseDTOList);

    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable Long id){
        return ResponseEntity.ok().body(categoryService.getCategoryById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO){

        CategoryResponseDTO categoryResponseDTO = categoryService.createCategory(categoryRequestDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDTO);
    }

    @DeleteMapping("/{id}")
    String  deleteCategory(@PathVariable("id") Long Id){
        categoryService.deleteCategory(Id);
        return "Category Deleted Successfully";
    }

}
