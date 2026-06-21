package com.phuckhang.digital_store.common.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    @Qualifier("cloudinaryExecutor")
    private final Executor executor;

    // UPLOAD ĐƠN (Dùng cho Logo Hãng, Avatar User...)
    public String uploadFile(MultipartFile file) throws IOException {
        String publicValue = UUID.randomUUID().toString();
        Map<String, Object> options = ObjectUtils.asMap("public_id", "digital_store/products/" + publicValue, "folder", "digital_store/products");
        Map result = cloudinary.uploader().upload(file.getBytes(), options);
        return result.get("secure_url").toString();
    }

    // UPLOAD HÀNG LOẠT (Dùng cho Album ảnh Sản phẩm)
//    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
//        List<String> imageUrls = new ArrayList<>();
//        for (MultipartFile file : files) {
//            if (file != null && !file.isEmpty()) {
//                String url = this.uploadFile(file);
//                imageUrls.add(url);
//            }
//        }
//        return imageUrls;
//    }
    public List<String> uploadFiles(List<MultipartFile> files) {

        // Tạo danh sách các tác vụ chạy ngầm song song
        List<CompletableFuture<String>> uploadFutures = files.stream()
                .filter(file -> file != null && !file.isEmpty())
                .map(file -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return this.uploadFile(file); // Mỗi luồng tự gọi API Cloudinary
                    } catch (IOException e) {
                        throw new RuntimeException("Lỗi upload ảnh: " + file.getOriginalFilename());
                    }
                }, executor)) // Giao cho Cloudinary-Async Thread thực hiện
                .toList();

        // Chờ tất cả luồng hoàn thành và bóc lấy kết quả URL
        return uploadFutures.stream()
                .map(CompletableFuture::join) // Dừng luồng chính chờ kết quả của từng luồng phụ
                .collect(Collectors.toList());
    }
}