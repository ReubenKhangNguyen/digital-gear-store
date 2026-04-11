package com.phuckhang.digital_store.catalog.service.implement;

import com.phuckhang.digital_store.catalog.dto.request.product.ProductCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductDetailResponseDTO;
import com.phuckhang.digital_store.catalog.dto.response.product.ProductListResponseDTO;
import com.phuckhang.digital_store.catalog.entity.Brand;
import com.phuckhang.digital_store.catalog.entity.Category;
import com.phuckhang.digital_store.catalog.entity.Product;
import com.phuckhang.digital_store.catalog.entity.ProductImage;
import com.phuckhang.digital_store.catalog.enums.ProductStatus;
import com.phuckhang.digital_store.catalog.mapper.ProductMapper;
import com.phuckhang.digital_store.catalog.repository.BrandRepository;
import com.phuckhang.digital_store.catalog.repository.CategoryRepository;
import com.phuckhang.digital_store.catalog.repository.ProductRepository;
import com.phuckhang.digital_store.catalog.service.ProductService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    BrandRepository brandRepository;
    ProductMapper productMapper;

    @Override
    @Transactional
    public ProductDetailResponseDTO createProduct(ProductCreateRequestDTO requestDTO) {
        if (productRepository.existsBySku(requestDTO.getSku())) {
            throw new RuntimeException("Mã SKU '" + requestDTO.getSku() + "' đã tồn tại trong hệ thống!");
        }

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại!"));
        Brand brand = brandRepository.findById(requestDTO.getBrandId())
                .orElseThrow(() -> new RuntimeException("Thương hiệu không tồn tại!"));


        Product newProduct = productMapper.toEntity(requestDTO);
        newProduct.setStatus(ProductStatus.ACTIVE);


        newProduct.setCategory(category);
        newProduct.setBrand(brand);

        //  (Bi-directional mapping)
        if (requestDTO.getImages() != null && !requestDTO.getImages().isEmpty()) {
            List<ProductImage> imageEntities = new ArrayList<>();

            requestDTO.getImages().forEach(imageDTO -> {
                ProductImage imageEntity = new ProductImage();
                imageEntity.setImageUrl(imageDTO.getImageUrl());
                imageEntity.setIsThumbnail(imageDTO.getIsThumbnail());

                imageEntity.setProduct(newProduct);

                imageEntities.add(imageEntity);
            });

            newProduct.setProductImages(imageEntities);
        }

        Product savedProduct = productRepository.save(newProduct);

        return productMapper.toDetailResponseDTO(savedProduct);
    }

    @Override
    public ProductDetailResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        return productMapper.toDetailResponseDTO(product);
    }

    @Override
    public List<ProductListResponseDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream()
                .filter(p -> p.getStatus() == ProductStatus.ACTIVE)
                .map(productMapper::toListResponseDTO)
                .toList();
    }
}
