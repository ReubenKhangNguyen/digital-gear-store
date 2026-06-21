package com.phuckhang.digital_store.iam.service.implement;

import com.phuckhang.digital_store.common.exception.AppException;
import com.phuckhang.digital_store.common.exception.ErrorCode;
import com.phuckhang.digital_store.iam.dto.request.UserAddressRequestDTO;
import com.phuckhang.digital_store.iam.dto.response.UserAddressResponseDTO;
import com.phuckhang.digital_store.iam.entity.User;
import com.phuckhang.digital_store.iam.entity.UserAddress;
import com.phuckhang.digital_store.iam.mapper.UserAddressMapper;
import com.phuckhang.digital_store.iam.repository.UserAddressRepository;
import com.phuckhang.digital_store.iam.repository.UserRepository;
import com.phuckhang.digital_store.iam.service.UserAddressService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserAddressServiceImpl implements UserAddressService {
    UserRepository userRepository;
    UserAddressRepository userAddressRepository;
    UserAddressMapper userAddressMapper;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
    private void untoggleCurrentDefaultAddress(String userId) {
        userAddressRepository.findByUserIdAndIsDefaultTrue(userId).ifPresent(oldDefault -> {
            oldDefault.setIsDefault(false);
            userAddressRepository.save(oldDefault);
        });
    }
    // ----------------------
    @Override
    @Transactional(readOnly = true)
    public List<UserAddressResponseDTO> getMyAddresses() {
        User user = getCurrentUser();
        List<UserAddress> addresses = userAddressRepository.findByUserId(user.getId());
        return addresses.stream().map(userAddressMapper::toUserAddressResponseDTO).toList();
    }
    @Override
    @Transactional
    public UserAddressResponseDTO createAddress(UserAddressRequestDTO requestDTO) {
        User user = getCurrentUser();
        List<UserAddress> currentAddresses = userAddressRepository.findByUserId(user.getId());
        UserAddress newAddress = userAddressMapper.toUserAddress(requestDTO);
        newAddress.setUser(user);
        // Nếu là địa chỉ đầu tiên -> Bắt buộc là Default
        if (currentAddresses.isEmpty()) {
            newAddress.setIsDefault(true);
        } else {
            // Nếu khách chọn làm Default -> Phải tắt cờ cũ đi
            if (Boolean.TRUE.equals(requestDTO.getIsDefault())) {
                untoggleCurrentDefaultAddress(user.getId());
            } else {
                newAddress.setIsDefault(false);
            }
        }
        return userAddressMapper.toUserAddressResponseDTO(userAddressRepository.save(newAddress));
    }
    @Override
    @Transactional
    public UserAddressResponseDTO updateAddress(Long addressId, UserAddressRequestDTO requestDTO) {
        User user = getCurrentUser();
        UserAddress existingAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND)); // Vui lòng tạo ErrorCode này
        // BẢO MẬT: Chống lỗi IDOR
        if (!existingAddress.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // Xử lý cờ Default
        if (Boolean.TRUE.equals(requestDTO.getIsDefault()) && !existingAddress.getIsDefault()) {
            untoggleCurrentDefaultAddress(user.getId());
        }
        userAddressMapper.updateAddressFromDTO(requestDTO, existingAddress);
        return userAddressMapper.toUserAddressResponseDTO(userAddressRepository.save(existingAddress));
    }
    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        User user = getCurrentUser();
        UserAddress addressToDelete = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        if (!addressToDelete.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        boolean wasDefault = addressToDelete.getIsDefault();
        userAddressRepository.delete(addressToDelete);
        // NGHIỆP VỤ NÂNG CAO: Nếu vừa xóa địa chỉ mặc định, bốc 1 địa chỉ khác lên làm mặc định thay thế
        if (wasDefault) {
            List<UserAddress> remainingAddresses = userAddressRepository.findByUserId(user.getId());
            if (!remainingAddresses.isEmpty()) {
                // Ta lấy đại địa chỉ đầu tiên trong danh sách (có thể tối ưu lấy địa chỉ mới nhất sau)
                UserAddress newDefault = remainingAddresses.get(0);
                newDefault.setIsDefault(true);
                userAddressRepository.save(newDefault);
            }
        }
    }
    @Override
    @Transactional
    public void setDefaultAddress(Long addressId) {
        User user = getCurrentUser();
        UserAddress targetAddress = userAddressRepository.findById(addressId)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));
        if (!targetAddress.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        if (targetAddress.getIsDefault()) {
            return; // Đã là mặc định rồi thì không làm gì cả
        }
        untoggleCurrentDefaultAddress(user.getId());

        targetAddress.setIsDefault(true);
        userAddressRepository.save(targetAddress);
    }
}
