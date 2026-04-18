package com.phuckhang.digital_store.catalog.controller;

import com.phuckhang.digital_store.catalog.dto.request.category.CategoryRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.category.CategoryResponseDTO;
import com.phuckhang.digital_store.catalog.service.CategoryService;
import com.phuckhang.digital_store.common.dto.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
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
    public ApiResponse<List<CategoryResponseDTO>> getCategoryTree() {
        return ApiResponse.<List<CategoryResponseDTO>>builder()
                .result(categoryService.getCategoryTree())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CategoryResponseDTO> getCategoryById(@PathVariable Long id) {
        return ApiResponse.<CategoryResponseDTO>builder()
                .result(categoryService.getCategoryById(id))
                .build();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Cực kỳ quan trọng: Giữ nguyên mã 201 Created của HTTP
    public ApiResponse<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        return ApiResponse.<CategoryResponseDTO>builder()
                .message("Tạo danh mục thành công") // Có thể truyền thêm lời nhắn nếu thích
                .result(categoryService.createCategory(categoryRequestDTO))
                .build();
    }

//    Test thôi chưa làm xóa mềm
//    @DeleteMapping("/{id}")
//    public ApiResponse<String> deleteCategory(@PathVariable("id") Long id) {
//        categoryService.deleteCategory(id);
//        return ApiResponse.<String>builder()
//                .message("Xóa danh mục thành công")
//                .build(); // Không có result, chỉ trả về message và code 1000
//    }
}
