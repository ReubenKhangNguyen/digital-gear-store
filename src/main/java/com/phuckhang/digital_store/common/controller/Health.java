package com.phuckhang.digital_store.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
public class Health {
    @GetMapping ("/health")
    public ResponseEntity <String> health() {
        return ResponseEntity.ok("Hệ thống Cửa hàng thiết bị số của Phúc Khang đang chạy rất mượt mà!");
    }
}
