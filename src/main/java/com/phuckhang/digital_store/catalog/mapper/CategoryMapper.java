package com.phuckhang.digital_store.catalog.mapper;

import com.phuckhang.digital_store.catalog.dto.request.category.CategoryRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.category.CategoryResponseDTO;
import com.phuckhang.digital_store.catalog.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper (componentModel = "spring")
public interface CategoryMapper {

    @Mapping(ignore = true, target = "categoryParent")
    @Mapping(target = "categoryChild", ignore = true)
    Category toEntity(CategoryRequestDTO categoryRequestDTO);

    @Mapping(source = "categoryParent.id", target = "parentId")
    @Mapping(ignore = true,target = "categoryChild")
    CategoryResponseDTO toCategoryResponseDTO(Category category);

    @Mapping(source = "parentId" , target = "categoryParent.id")
    @Mapping(target = "categoryChild", ignore = true)
    void updateCategory(CategoryRequestDTO categoryRequestDTO, @MappingTarget Category category);


//    @Mapping(target = "parentCategory", ignore = true) // bỏ qua, service sẽ gán
//    @Mapping(target = "children", ignore = true)
//    Category toEntity(CategoryRequestDTO categoryRequestDTO);
//
//    @Mapping(source = "parentCategory.id", target = "parentId")
//    CategoryResponseDTO toCategoryResponseDTO(Category category);
//
//    @Mapping(target = "parentCategory", ignore = true)
//    @Mapping(target = "children", ignore = true)
//    void updateCategory(CategoryRequestDTO categoryRequestDTO, @MappingTarget Category category);

}
