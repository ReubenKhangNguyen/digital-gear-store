package com.phuckhang.digital_store.catalog.service.implement;

import com.phuckhang.digital_store.catalog.dto.request.product.ProductCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.request.product.ProductUpdateRequestDTO;
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
import com.phuckhang.digital_store.common.exception.AppException;
import com.phuckhang.digital_store.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
            throw new AppException(ErrorCode.PRODUCT_SKU_EXISTED);
        }

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Brand brand = brandRepository.findById(requestDTO.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));


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
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

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

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public ProductDetailResponseDTO updateProduct(Long id, ProductUpdateRequestDTO requestDTO) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (!product.getSku().equals(requestDTO.getSku()) && productRepository.existsBySku(requestDTO.getSku())) {
            throw new AppException(ErrorCode.PRODUCT_SKU_EXISTED);
        }

        Category category = categoryRepository.findById(requestDTO.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));
        Brand brand = brandRepository.findById(requestDTO.getBrandId())
                .orElseThrow(() -> new AppException(ErrorCode.BRAND_NOT_FOUND));

        productMapper.updateProduct(product, requestDTO);

        product.setCategory(category);
        product.setBrand(brand);

        if (requestDTO.getImages() != null) {
            product.getProductImages().clear();
            requestDTO.getImages().forEach(imageDTO -> {
                ProductImage imageEntity = new ProductImage();
                imageEntity.setImageUrl(imageDTO.getImageUrl());
                imageEntity.setIsThumbnail(imageDTO.getIsThumbnail());
                imageEntity.setProduct(product);
                product.getProductImages().add(imageEntity);
            });
        }

        Product savedProduct = productRepository.save(product);

        return productMapper.toDetailResponseDTO(savedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    @Transactional
    public String deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));
        product.setStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
        return "Xóa sản phẩm thành công";
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductListResponseDTO> searchProducts(String keyword, List<Integer> categoryIds, List<Integer> brandIds, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {

        // Khởi tạo phễu lọc mặc định: Chỉ lấy hàng đang bán
        Specification<Product> spec = Specification.where(ProductSpecification.isActiveTrue());
        // Lắp ráp các điều kiện động (Dynamic)
        if (keyword != null && !keyword.trim().isEmpty()) {
            spec = spec.and(ProductSpecification.hasNameLike(keyword.trim()));
        }
        if (categoryIds != null && !categoryIds.isEmpty()) {
            spec = spec.and(ProductSpecification.hasCategoryIds(categoryIds));
        }
        if (brandIds != null && !brandIds.isEmpty()) {
            spec = spec.and(ProductSpecification.hasBrandIds(brandIds));
        }
        // Xử lý logic lọc giá rất thông minh
        if (minPrice != null && maxPrice != null) {
            spec = spec.and(ProductSpecification.priceBetween(minPrice, maxPrice));
        } else if (minPrice != null) {
            spec = spec.and(ProductSpecification.priceGreaterThanEqual(minPrice));
        } else if (maxPrice != null) {
            spec = spec.and(ProductSpecification.priceLessThanEqual(maxPrice));
        }
        // Truyền cục Specification khổng lồ này xuống Database
        Page<Product> productPage = productRepository.findAll(spec, pageable);

        return productPage.map(productMapper::toListResponseDTO);
    }
}
