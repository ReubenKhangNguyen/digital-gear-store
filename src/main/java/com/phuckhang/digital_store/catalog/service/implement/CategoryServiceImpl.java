    package com.phuckhang.digital_store.catalog.service.implement;

    import com.phuckhang.digital_store.catalog.dto.request.category.CategoryRequestDTO;
    import com.phuckhang.digital_store.catalog.dto.response.category.CategoryResponseDTO;
    import com.phuckhang.digital_store.catalog.entity.Category;
    import com.phuckhang.digital_store.catalog.enums.CategoryStatus;
    import com.phuckhang.digital_store.catalog.mapper.CategoryMapper;
    import com.phuckhang.digital_store.catalog.repository.CategoryRepository;
    import com.phuckhang.digital_store.catalog.service.CategoryService;
    import jakarta.transaction.Transactional;
    import lombok.AccessLevel;
    import lombok.RequiredArgsConstructor;
    import lombok.experimental.FieldDefaults;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Map;
    import java.util.stream.Collectors;

    @Service
    @Slf4j
    @RequiredArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
    public class CategoryServiceImpl implements CategoryService {

        CategoryRepository categoryRepository;
        CategoryMapper categoryMapper;


        @Override
        @Transactional
        public CategoryResponseDTO createCategory(CategoryRequestDTO requestDTO) {
            // check trùng tên
            if (categoryRepository.existsByName(requestDTO.getName())) {
                throw new RuntimeException("Danh mục '" + requestDTO.getName() + "' đã tồn tại trong hệ thống!");
            }

            Category newCategory = categoryMapper.toEntity(requestDTO);

            // check danh mục cha để gán vào đối tượng CategoryParen trong newCategory
            if (requestDTO.getParentId() != null) {
                Category parentCategory = categoryRepository.findById(requestDTO.getParentId())
                        .orElseThrow(() -> new RuntimeException("Danh mục cha không tồn tại!"));

                if (parentCategory.getCategoryStatus() == CategoryStatus.DELETED){
                    throw  new RuntimeException("Không thể thêm vào danh mục đã bị xóa!");
                }

                // gán parentCategory cho đối tượng CategoryParen trong newCategory
                newCategory.setCategoryParent(parentCategory);
            }

            // mặc định là active
            newCategory.setCategoryStatus(CategoryStatus.ACTIVE);

            // luu xuong db
            Category savedCategory = categoryRepository.save(newCategory);

            // trả về cho controller
            return categoryMapper.toCategoryResponseDTO(savedCategory);

        }



        @Override
        public List<CategoryResponseDTO> getCategoryTree() {

            // lấy danh sách category từ db lên

            List<Category> allCategories = categoryRepository.findAllByCategoryStatus(CategoryStatus.ACTIVE);

            // map từng category tới dto
            List<CategoryResponseDTO> allCategoryResponseDTO = allCategories.stream()
                    .map(categoryMapper::toCategoryResponseDTO)
                    .toList();

            // Key: ID của Danh mục, Value: Chính cái object DTO đó
            Map<Long, CategoryResponseDTO> dtoMap = allCategoryResponseDTO.stream()
                    .collect(Collectors.toMap(CategoryResponseDTO::getId, dto -> dto));

            // tạo cây
            List<CategoryResponseDTO> rootCategories = new ArrayList<>();

            // duyệt từng đối tượng trong dto để đưa vào cây
            for (CategoryResponseDTO dto : allCategoryResponseDTO) {
                // đưa danh mục có cấp lớn nhất vào cây
                if (dto.getParentId() == null){
                    rootCategories.add(dto);
                }
                // gán object danh mục con hiện tại vào danh mục cha ở trên 1 cấp
                else {
                    CategoryResponseDTO parent = dtoMap.get(dto.getParentId());

                    if (parent != null) {
                        if (parent.getCategoryChild() == null) {
                            parent.setCategoryChild(new ArrayList<>());
                        }

                        parent.getCategoryChild().add(dto);
                    }
                }
            }

            // trả về cây danh mục
            return rootCategories;
        }

        @Override
        public CategoryResponseDTO getCategoryById(Long id) {
            Category category = categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với ID: " + id));

            return categoryMapper.toCategoryResponseDTO(category);
        }

        @Override
        @Transactional
        public CategoryResponseDTO updateCategory(Long Id, CategoryRequestDTO requestDTO) {

            Category category = categoryRepository.findById((Id)).orElseThrow(() ->new RuntimeException("Không tìm thấy danh mục cần sửa"));

            if (!category.getName().equals(requestDTO.getName()) && categoryRepository.existsByName(requestDTO.getName())) {
                throw new RuntimeException("Tên danh mục '" + requestDTO.getName() + "' đã bị trùng với danh mục khác!");
            }

            if (requestDTO.getParentId() != null) {
                if (requestDTO.getParentId().equals(Id)){
                    throw new RuntimeException("Lỗi Logic: Một danh mục không thể tự nhận chính nó làm cha!");
                }
                Category categoryParent = categoryRepository.findById(category.getId()).orElseThrow(() -> new RuntimeException("Danh mục cha không tồn tại!"));
            }
            else {
                category.setCategoryParent(null);
            }

            categoryMapper.updateCategory(requestDTO, category);

            Category updateCategory = categoryRepository.save(category);

            return categoryMapper.toCategoryResponseDTO(updateCategory);
        }


        @Override
        public void deleteCategory(Long Id) {
    //      Xóa mềm
            Category category = categoryRepository.findById((Id)).orElseThrow(() ->new RuntimeException("Không tìm thấy danh mục để xóa"));

            boolean checkActiveChild = category.getCategoryChild().stream().anyMatch(child -> child.getCategoryStatus() != CategoryStatus.DELETED);
            if (checkActiveChild) {
                throw new RuntimeException("\"CHẶN XÓA: Không thể xóa danh mục này vì vẫn còn danh mục con đang hoạt động bên trong!\"");
            }

            category.setCategoryStatus(CategoryStatus.DELETED);

            categoryRepository.save(category);
    //      Xóa hẳn
    //      categoryRepository.deleteById(Id);
        }
    }
