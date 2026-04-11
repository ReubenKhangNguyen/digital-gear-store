package com.phuckhang.digital_store.common.controller;

import com.phuckhang.digital_store.common.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/files")
@CrossOrigin("*")
@RequiredArgsConstructor
public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    // API Upload 1 file (Giữ lại nếu cần)
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            return ResponseEntity.ok(cloudinaryService.uploadFile(file));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Lỗi: " + e.getMessage());
        }
    }

    // API UPLOAD NHIỀU FILE CÙNG LÚC
    @PostMapping("/upload-multiple")
    public ResponseEntity<?> uploadMultipleImages(@RequestParam("files") List<MultipartFile> files) {
        try {
            // Nhận vào 5 file -> Service xử lý -> Trả ra 1 cái List chứa 5 đường link URL
            List<String> uploadedUrls = cloudinaryService.uploadFiles(files);
            return ResponseEntity.ok(uploadedUrls);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Lỗi upload nhiều file: " + e.getMessage());
        }
    }
}