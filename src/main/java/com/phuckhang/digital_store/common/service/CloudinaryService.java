package com.phuckhang.digital_store.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    // ========================================================
    // HÀM 1: UPLOAD ĐƠN (Dùng cho Logo Hãng, Avatar User...)
    // ========================================================
    public String uploadFile(MultipartFile file) throws IOException {
        String publicValue = UUID.randomUUID().toString();
        Map<String, Object> options = ObjectUtils.asMap(
                "public_id", "digital_store/products/" + publicValue,
                "folder", "digital_store/products"
        );
        Map result = cloudinary.uploader().upload(file.getBytes(), options);
        return result.get("secure_url").toString();
    }

    // ========================================================
    // HÀM 2: UPLOAD HÀNG LOẠT (Dùng cho Album ảnh Sản phẩm)
    // ========================================================
    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        // Lặp qua từng file trong danh sách gửi lên
        for (MultipartFile file : files) {
            // Kiểm tra tránh trường hợp file rỗng (hoặc user bấm gửi mà chưa chọn ảnh)
            if (file != null && !file.isEmpty()) {
                // Tái sử dụng lại hàm Upload Đơn ở trên
                String url = this.uploadFile(file);
                imageUrls.add(url);
            }
        }

        // Trả về một mảng chứa 5-6 cái link ảnh
        return imageUrls;
    }
}