package com.phuckhang.digital_store.catalog.service.implement;

import com.phuckhang.digital_store.catalog.dto.request.brand.BrandCreateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.request.brand.BrandUpdateRequestDTO;
import com.phuckhang.digital_store.catalog.dto.response.brand.BrandResponseDTO;
import com.phuckhang.digital_store.catalog.entity.Brand;
import com.phuckhang.digital_store.catalog.enums.BrandStatus;
import com.phuckhang.digital_store.catalog.mapper.BrandMapper;
import com.phuckhang.digital_store.catalog.repository.BrandRepository;
import com.phuckhang.digital_store.catalog.service.BrandService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BrandServiceImpl implements BrandService {
    // Tiêm các dependency cần thiết
    BrandRepository brandRepository;
    BrandMapper brandMapper;

    @Override
    @Transactional
    public BrandResponseDTO createBrand(BrandCreateRequestDTO requestDTO) {

        if (brandRepository.existsByName(requestDTO.getName())) {
            throw new RuntimeException("Brand '\" + requestDTO.getName() + \"' đã tồn tại trong hệ thống!!");
        }

        Brand newBrand = brandMapper.toEntity(requestDTO);

        newBrand.setStatus(BrandStatus.ACTIVE);

        Brand savedBrand = brandRepository.save(newBrand);

        return brandMapper.toBrandResponseDTO(savedBrand);
    }

    @Override
    public List<BrandResponseDTO> getAllBrands() {

        List<Brand>  allBrands = brandRepository.findAll();

        return allBrands.stream()
                .map(brandMapper::toBrandResponseDTO)
                .toList();
    }

    @Override
    public BrandResponseDTO getBrandById(Long id) {

        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Brand với ID: " + id));

        return brandMapper.toBrandResponseDTO(brand);
    }

    @Override
    @Transactional
    public BrandResponseDTO updateBrand(Long id, BrandUpdateRequestDTO requestDTO) {

        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Brand cần sửa"));

        if (!brand.getName().equals(requestDTO.getName()) && brandRepository.existsByName(requestDTO.getName())) {
            throw new RuntimeException("Tên Brand '" + requestDTO.getName() + "' đã bị trùng với tên Brand khác!");
        }

        brandMapper.updateBrand(requestDTO, brand);

        Brand savedBrand = brandRepository.save(brand);

        return brandMapper.toBrandResponseDTO(savedBrand);
    }

    @Override
    @Transactional
    public void deleteBrand(Long id) {

        Brand brand = brandRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy Brand với ID: " + id));

        brand.setStatus(BrandStatus.INACTIVE);

        brandRepository.save(brand);
    }

}
