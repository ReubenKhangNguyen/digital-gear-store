package com.phuckhang.digital_store.common.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "cloudinaryExecutor")
    public Executor cloudinaryExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // Số luồng luôn sẵn sàng
        executor.setMaxPoolSize(20);  // Tối đa 20 luồng khi cao điểm
        executor.setQueueCapacity(50); // Hàng đợi nếu 20 luồng đang bận
        executor.setThreadNamePrefix("Cloudinary-Async-");
        executor.initialize();
        return executor;
    }
}
