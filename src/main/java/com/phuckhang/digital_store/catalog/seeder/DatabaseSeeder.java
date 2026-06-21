package com.phuckhang.digital_store.catalog.seeder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phuckhang.digital_store.catalog.dto.request.product.ProductCreateRequestDTO;
import com.phuckhang.digital_store.catalog.repository.ProductRepository;
import com.phuckhang.digital_store.catalog.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        // Khởi tạo ObjectMapper thủ công để tránh lỗi thiếu Bean của Spring
        ObjectMapper objectMapper = new ObjectMapper();

        // Kiểm tra xem DB đã có dữ liệu sản phẩm hay chưa
        if (productRepository.count() < 15) {
            log.info("Bắt đầu tiến trình tự động Seed Data 15 sản phẩm...");
            try {
                // Ép kiểu chuỗi JSON thành danh sách DTO
                List<ProductCreateRequestDTO> seedProducts = objectMapper.readValue(PRODUCT_SEED_JSON, new TypeReference<List<ProductCreateRequestDTO>>() {});
                
                int count = 0;
                for (ProductCreateRequestDTO dto : seedProducts) {
                    try {
                        productService.createProduct(dto);
                        count++;
                        log.info("Đã seed thành công: {}", dto.getName());
                    } catch (Exception e) {
                        // Nếu bắt gặp lỗi trùng SKU hoặc lỗi khác, bỏ qua và đi tiếp
                        log.warn("Bỏ qua sản phẩm {} do lỗi: {}", dto.getName(), e.getMessage());
                    }
                }
                log.info("Quá trình Seed Data hoàn tất! Đã thêm {} sản phẩm mới.", count);
            } catch (Exception e) {
                log.error("Lỗi khi đọc chuỗi JSON Seed Data: {}", e.getMessage());
            }
        } else {
            log.info("Database đã có sẵn dữ liệu sản phẩm. Bỏ qua bước Seed Data.");
        }
    }

    // Chuỗi JSON nguyên bản chứa 15 sản phẩm từ PDF
    private static final String PRODUCT_SEED_JSON = "[\n" +
            " {\n" +
            " \"name\": \"MacBook Air 13 inch M3 2024 (16GB/512GB) - Midnight\",\n" +
            " \"sku\": \"LAP-APP-MBA13-M3-16-512-MID-001\",\n" +
            " \"price\": 23500000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 30,\n" +
            " \"brandId\": 1,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"os\": \"macOS\",\n" +
            " \"cpu_type\": \"Apple M3\",\n" +
            " \"cpu_detail\": \"Apple M3 8 nhân CPU (4 nhân hiệu năng cao, 4 nhân tiết kiệm điện)\",\n" +
            " \"gpu\": \"10 nhân GPU, 16 nhân Neural Engine\",\n" +
            " \"ram_capacity\": \"16GB\",\n" +
            " \"ram_detail\": \"16GB Unified Memory\",\n" +
            " \"storage_capacity\": \"512GB\",\n" +
            " \"storage_detail\": \"512GB SSD\",\n" +
            " \"screen_size\": \"13.6 inches\",\n" +
            " \"screen_detail\": \"Liquid Retina 2560 x 1664 pixels, 500 nits, True Tone, P3 wide color\",\n" +
            " \"webcam\": \"FaceTime HD 1080p\",\n" +
            " \"audio\": \"Hệ thống 4 loa, Spatial Audio, 3 microphone\",\n" +
            " \"ports\": \"MagSafe 3, 2x Thunderbolt/USB 4, Jack 3.5mm\",\n" +
            " \"wireless\": \"Wi-Fi 6E, Bluetooth 5.3\",\n" +
            " \"battery\": \"Lên đến 18 giờ xem video Apple TV app\",\n" +
            " \"security\": \"Touch ID\",\n" +
            " \"dimensions\": \"1.13 x 30.41 x 21.50 cm\",\n" +
            " \"weight\": \"1.24 kg\",\n" +
            " \"color\": \"Midnight\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"ASUS ROG Strix G16 G614JV-N4369W (i7-13650HX/16GB/1TB/RTX 4060) - Xám\",\n" +
            " \"sku\": \"LAP-ASU-ROGG16-G614JV-I7-RTX4060-001\",\n" +
            " \"price\": 33890000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 26,\n" +
            " \"brandId\": 6,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"os\": \"Windows 11 Home\",\n" +
            " \"cpu_type\": \"Intel Core i7\",\n" +
            " \"cpu_detail\": \"Intel Core i7-13650HX, 14 nhân (6 P-cores + 8 E-cores), 20 luồng, tối đa 4.9GHz, 24MB cache\",\n" +
            " \"gpu\": \"NVIDIA GeForce RTX 4060 Laptop GPU 8GB GDDR6, MUX Switch, NVIDIA Advanced Optimus\",\n" +
            " \"ram_capacity\": \"16GB\",\n" +
            " \"ram_detail\": \"16GB DDR5-4800 SO-DIMM, hỗ trợ nâng cấp tối đa 32GB\",\n" +
            " \"storage_capacity\": \"1TB\",\n" +
            " \"storage_detail\": \"1TB PCIe 4.0 NVMe M.2 SSD\",\n" +
            " \"screen_size\": \"16 inches\",\n" +
            " \"screen_detail\": \"WUXGA 1920 x 1200, 16:10, IPS, 240Hz, 100% sRGB, Anti-glare\",\n" +
            " \"keyboard\": \"Backlit Chiclet Keyboard RGB 4 vùng\",\n" +
            " \"ports\": \"USB-C, USB-A, HDMI, LAN RJ45, Jack 3.5mm, DC-in\",\n" +
            " \"wireless\": \"Wi-Fi 6E, Bluetooth\",\n" +
            " \"battery\": \"4-cell 90Wh\",\n" +
            " \"dimensions\": \"35.4 x 26.4 x 2.26-3.04 cm\",\n" +
            " \"weight\": \"2.50 kg\",\n" +
            " \"color\": \"Eclipse Gray\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Acer Predator Helios Neo 16 PHN16-71-76H5 (i7-13650HX/16GB/512GB/RTX 4060) - Đen\",\n" +
            " \"sku\": \"LAP-ACE-PHN16-71-I7-RTX4060-001\",\n" +
            " \"price\": 24990000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 28,\n" +
            " \"brandId\": 9,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"os\": \"Windows 11 Home\",\n" +
            " \"cpu_type\": \"Intel Core i7\",\n" +
            " \"cpu_detail\": \"Intel Core i7-13650HX, 14 nhân/20 luồng, tối đa 4.90GHz, 24MB Intel Smart Cache\",\n" +
            " \"gpu\": \"NVIDIA GeForce RTX 4060 8GB GDDR6\",\n" +
            " \"ram_capacity\": \"16GB\",\n" +
            " \"ram_detail\": \"16GB DDR5 4800MHz, hỗ trợ nâng cấp\",\n" +
            " \"storage_capacity\": \"512GB\",\n" +
            " \"storage_detail\": \"512GB M.2 PCIe NVMe SSD\",\n" +
            " \"screen_size\": \"16 inches\",\n" +
            " \"screen_detail\": \"WUXGA 1920 x 1200, IPS, 165Hz\",\n" +
            " \"ports\": \"USB-C, USB-A, HDMI, LAN RJ45, Audio combo\",\n" +
            " \"wireless\": \"Wi-Fi 6/6E, Bluetooth\",\n" +
            " \"keyboard\": \"Backlit keyboard\",\n" +
            " \"color\": \"Black\",\n" +
            " \"series\": \"Predator Helios Neo 16\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Lenovo LOQ 15IRX9 83DV000MVN (i5-13450HX/16GB/512GB/RTX 4050) - Xám\",\n" +
            " \"sku\": \"LAP-LEN-LOQ15IRX9-I5-RTX4050-001\",\n" +
            " \"price\": 26090000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 33,\n" +
            " \"brandId\": 8,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"os\": \"Windows 11 Home\",\n" +
            " \"cpu_type\": \"Intel Core i5\",\n" +
            " \"cpu_detail\": \"Intel Core i5-13450HX, 10 nhân/16 luồng, 20MB cache, tối đa 4.6GHz\",\n" +
            " \"gpu\": \"NVIDIA GeForce RTX 4050 Laptop GPU 6GB GDDR6\",\n" +
            " \"ram_capacity\": \"16GB\",\n" +
            " \"ram_detail\": \"16GB DDR5-4800 SO-DIMM, hỗ trợ nâng cấp tối đa 32GB\",\n" +
            " \"storage_capacity\": \"512GB\",\n" +
            " \"storage_detail\": \"512GB PCIe 4.0 NVMe M.2 SSD\",\n" +
            " \"screen_size\": \"15.6 inches\",\n" +
            " \"screen_detail\": \"FHD 1920 x 1080, IPS, 300 nits, Anti-glare, 100% sRGB, 144Hz, G-SYNC\",\n" +
            " \"keyboard\": \"4-zone RGB backlit keyboard\",\n" +
            " \"ports\": \"USB-C, USB-A, HDMI, RJ45, Audio combo\",\n" +
            " \"wireless\": \"Wi-Fi 6, Bluetooth 5.1\",\n" +
            " \"battery\": \"Integrated battery, sạc nhanh tùy cấu hình\",\n" +
            " \"color\": \"Storm Grey\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Samsung Galaxy S24 Ultra 5G 256GB - Xám Titan\",\n" +
            " \"sku\": \"MOB-SAM-S24U-12-256-GT-001\",\n" +
            " \"price\": 15990000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 17,\n" +
            " \"brandId\": 2,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"os\": \"Android 14, One UI 6.1\",\n" +
            " \"series\": \"Galaxy S24 Ultra\",\n" +
            " \"sim\": \"2 Nano SIM hoặc Nano SIM + eSIM, hỗ trợ 5G\",\n" +
            " \"color\": \"Xám Titan\",\n" +
            " \"material\": \"Khung Titanium, kính cường lực Gorilla Armor\",\n" +
            " \"cpu_detail\": \"Snapdragon 8 Gen 3 for Galaxy\",\n" +
            " \"gpu\": \"Adreno GPU tích hợp\",\n" +
            " \"ram_capacity\": \"12GB\",\n" +
            " \"storage_capacity\": \"256GB\",\n" +
            " \"screen_size\": \"6.8 inches\",\n" +
            " \"screen_tech\": \"Dynamic AMOLED 2X QHD+, 120Hz, HDR10+, độ sáng cao\",\n" +
            " \"camera_rear\": \"200MP chính + 50MP tele 5x + 10MP tele 3x + 12MP góc siêu rộng\",\n" +
            " \"camera_front\": \"12MP\",\n" +
            " \"battery_detail\": \"5000mAh, sạc nhanh có dây, sạc không dây\",\n" +
            " \"charging_port\": \"USB Type-C\",\n" +
            " \"wireless\": \"Wi-Fi 7, Bluetooth 5.3, NFC, 5G\",\n" +
            " \"security\": \"Vân tay siêu âm dưới màn hình, mở khóa khuôn mặt\",\n" +
            " \"features\": \"S Pen, IP68, Galaxy AI, Samsung DeX\",\n" +
            " \"dimensions_weight\": \"162.3 x 79.0 x 8.6 mm - 232g\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Xiaomi 14T Pro 5G 12GB/512GB - Xám Titan\",\n" +
            " \"sku\": \"MOB-XIA-14TP-12-512-GT-001\",\n" +
            " \"price\": 12990000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 17,\n" +
            " \"brandId\": 3,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"os\": \"Android với Xiaomi HyperOS\",\n" +
            " \"series\": \"Xiaomi 14T Pro\",\n" +
            " \"sim\": \"Dual SIM, hỗ trợ 5G\",\n" +
            " \"color\": \"Xám Titan\",\n" +
            " \"material\": \"Khung kim loại, mặt lưng kính/tùy màu\",\n" +
            " \"cpu_detail\": \"MediaTek Dimensity 9300+ 4nm, CPU 1x Cortex-X4 3.4GHz + 3x Cortex-X4 2.85GHz + 4x Cortex-A720 2.0GHz\",\n" +
            " \"gpu\": \"Immortalis-G720 MC12\",\n" +
            " \"ram_capacity\": \"12GB\",\n" +
            " \"storage_capacity\": \"512GB\",\n" +
            " \"screen_size\": \"6.67 inches\",\n" +
            " \"screen_tech\": \"AMOLED 2712 x 1220, 144Hz, HDR, độ sáng cao\",\n" +
            " \"camera_rear\": \"Hệ thống 3 camera Leica: chính 50MP, tele 50MP, góc siêu rộng 12MP\",\n" +
            " \"camera_front\": \"32MP\",\n" +
            " \"battery_detail\": \"5000mAh, sạc nhanh HyperCharge 120W, hỗ trợ sạc không dây 50W tùy thị trường\",\n" +
            " \"charging_port\": \"USB Type-C\",\n" +
            " \"wireless\": \"Wi-Fi, Bluetooth, NFC, 5G\",\n" +
            " \"security\": \"Vân tay dưới màn hình, mở khóa khuôn mặt\",\n" +
            " \"features\": \"IP68, quay video 4K/8K tùy chế độ, AI photography\",\n" +
            " \"dimensions_weight\": \"160.4 x 75.1 x 8.39 mm - 209g\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"OPPO Reno12 Pro 5G 12GB/512GB - Xám\",\n" +
            " \"sku\": \"MOB-OPP-RENO12P-12-512-GR-001\",\n" +
            " \"price\": 13990000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 17,\n" +
            " \"brandId\": 4,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"os\": \"Android 14, ColorOS 14.1\",\n" +
            " \"series\": \"OPPO Reno12 Pro 5G\",\n" +
            " \"sim\": \"Dual SIM, hỗ trợ 5G\",\n" +
            " \"color\": \"Xám\",\n" +
            " \"cpu_detail\": \"MediaTek Dimensity 7300-Energy/Dimensity 7300 series tùy thị trường\",\n" +
            " \"gpu\": \"GPU tích hợp Mali\",\n" +
            " \"ram_capacity\": \"12GB\",\n" +
            " \"ram_detail\": \"12GB LPDDR4X\",\n" +
            " \"storage_capacity\": \"512GB\",\n" +
            " \"storage_detail\": \"UFS 3.1\",\n" +
            " \"screen_size\": \"6.7 inches\",\n" +
            " \"screen_tech\": \"AMOLED FHD+ 2412 x 1080, 120Hz, 1 tỷ màu, HDR10+\",\n" +
            " \"camera_rear\": \"50MP chính + 50MP tele portrait + 8MP góc siêu rộng\",\n" +
            " \"camera_front\": \"50MP\",\n" +
            " \"battery_detail\": \"5000mAh, sạc nhanh 80W SUPERVOOC\",\n" +
            " \"charging_port\": \"USB Type-C\",\n" +
            " \"wireless\": \"Wi-Fi, Bluetooth, NFC tùy thị trường, 5G\",\n" +
            " \"security\": \"Vân tay dưới màn hình, mở khóa khuôn mặt\",\n" +
            " \"features\": \"AI Eraser, AI Studio, IP65\",\n" +
            " \"dimensions_weight\": \"Khoảng 161.5 x 74.8 x 7.4 mm - 180g\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Google Pixel 8 Pro 12GB/128GB - Obsidian\",\n" +
            " \"sku\": \"MOB-GOO-PIXEL8P-12-128-OBS-001\",\n" +
            " \"price\": 14990000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 17,\n" +
            " \"brandId\": 21,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"os\": \"Android 14, hỗ trợ cập nhật dài hạn từ Google\",\n" +
            " \"series\": \"Google Pixel 8 Pro\",\n" +
            " \"sim\": \"Nano SIM + eSIM, hỗ trợ 5G\",\n" +
            " \"color\": \"Obsidian\",\n" +
            " \"material\": \"Khung nhôm, mặt lưng kính nhám, Gorilla Glass Victus 2\",\n" +
            " \"cpu_detail\": \"Google Tensor G3\",\n" +
            " \"gpu\": \"GPU tích hợp Immortalis-G715s MC10\",\n" +
            " \"ram_capacity\": \"12GB\",\n" +
            " \"storage_capacity\": \"128GB\",\n" +
            " \"screen_size\": \"6.7 inches\",\n" +
            " \"screen_tech\": \"Super Actua LTPO OLED 1344 x 2992, 1-120Hz, HDR, tối đa 2400 nits peak\",\n" +
            " \"camera_rear\": \"50MP chính + 48MP tele 5x + 48MP góc siêu rộng\",\n" +
            " \"camera_front\": \"10.5MP Dual PD selfie camera\",\n" +
            " \"battery_detail\": \"5050mAh, sạc nhanh có dây, sạc không dây\",\n" +
            " \"charging_port\": \"USB Type-C 3.2\",\n" +
            " \"wireless\": \"Wi-Fi 7, Bluetooth 5.3, NFC, UWB, 5G\",\n" +
            " \"security\": \"Vân tay dưới màn hình, Face Unlock\",\n" +
            " \"features\": \"IP68, AI camera, cảm biến nhiệt độ, Magic Editor\",\n" +
            " \"dimensions_weight\": \"162.6 x 76.5 x 8.8 mm - 213g\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Canon EOS R50 Kit RF-S 18-45mm IS STM - Đen\",\n" +
            " \"sku\": \"CAM-CAN-EOSR50-1845-BLK-001\",\n" +
            " \"price\": 17990000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 29,\n" +
            " \"brandId\": 11,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"type\": \"Mirrorless camera APS-C\",\n" +
            " \"sensor\": \"APS-C CMOS 22.3 x 14.9 mm\",\n" +
            " \"resolution\": \"24.2MP hiệu dụng\",\n" +
            " \"lens_mount\": \"Canon RF/RF-S\",\n" +
            " \"processor\": \"DIGIC X\",\n" +
            " \"iso_range\": \"ISO 100-32000, mở rộng tùy chế độ\",\n" +
            " \"autofocus\": \"Dual Pixel CMOS AF II, tối đa 651 vùng AF\",\n" +
            " \"continuous_shooting\": \"Tối đa 12 fps màn trập điện tử 1st curtain, 15 fps electronic shutter\",\n" +
            " \"video\": \"4K UHD oversampled từ 6K, Full HD tốc độ cao tùy chế độ\",\n" +
            " \"stabilization\": \"Ổn định hình ảnh phụ thuộc ống kính, Movie digital IS\",\n" +
            " \"viewfinder\": \"EVF OLED\",\n" +
            " \"screen\": \"LCD cảm ứng xoay lật 3.0 inches\",\n" +
            " \"storage\": \"SD/SDHC/SDXC UHS-I\",\n" +
            " \"connectivity\": \"Wi-Fi, Bluetooth, USB-C, HDMI micro\",\n" +
            " \"battery\": \"LP-E17\",\n" +
            " \"lens_included\": \"RF-S 18-45mm f/4.5-6.3 IS STM\",\n" +
            " \"weight\": \"Khoảng 375g gồm pin và thẻ nhớ\",\n" +
            " \"color\": \"Black\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Sony Alpha A7 IV Body (ILCE-7M4) - Đen\",\n" +
            " \"sku\": \"CAM-SON-A7IV-BODY-BLK-001\",\n" +
            " \"price\": 47490000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 25,\n" +
            " \"brandId\": 10,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"type\": \"Full-frame mirrorless camera\",\n" +
            " \"sensor\": \"Full-frame 35mm Exmor R CMOS 35.9 x 23.9 mm\",\n" +
            " \"resolution\": \"33MP hiệu dụng, khoảng 34.1MP tổng số điểm ảnh\",\n" +
            " \"lens_mount\": \"Sony E-mount\",\n" +
            " \"processor\": \"BIONZ XR\",\n" +
            " \"iso_range\": \"ISO 100-51200, mở rộng tùy chế độ\",\n" +
            " \"autofocus\": \"Fast Hybrid AF, Real-time Eye AF, nhận diện người/động vật/chim\",\n" +
            " \"continuous_shooting\": \"Tối đa 10 fps\",\n" +
            " \"video\": \"4K 60p 10-bit 4:2:2, S-Cinetone, S-Log3\",\n" +
            " \"stabilization\": \"Chống rung 5 trục trong thân máy\",\n" +
            " \"viewfinder\": \"EVF OLED 3.68 triệu điểm\",\n" +
            " \"screen\": \"LCD cảm ứng xoay lật đa góc\",\n" +
            " \"storage\": \"2 khe thẻ, hỗ trợ CFexpress Type A/SD tùy khe\",\n" +
            " \"connectivity\": \"Wi-Fi, Bluetooth, USB-C, HDMI, microphone/headphone\",\n" +
            " \"battery\": \"NP-FZ100\",\n" +
            " \"lens_included\": \"Body only\",\n" +
            " \"weight\": \"Khoảng 658g gồm pin và thẻ nhớ\",\n" +
            " \"color\": \"Black\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Fujifilm X-T5 Body - Bạc\",\n" +
            " \"sku\": \"CAM-FUJ-XT5-BODY-SIL-001\",\n" +
            " \"price\": 39990000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 24,\n" +
            " \"brandId\": 13,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"type\": \"Mirrorless camera APS-C\",\n" +
            " \"sensor\": \"APS-C X-Trans CMOS 5 HR BSI\",\n" +
            " \"resolution\": \"40.2MP\",\n" +
            " \"lens_mount\": \"Fujifilm X mount\",\n" +
            " \"processor\": \"X-Processor 5\",\n" +
            " \"iso_range\": \"ISO 125-12800, mở rộng tùy chế độ\",\n" +
            " \"autofocus\": \"Hybrid AF với nhận diện chủ thể bằng AI\",\n" +
            " \"continuous_shooting\": \"Tối đa 15 fps cơ, cao hơn với electronic shutter tùy crop/chế độ\",\n" +
            " \"video\": \"6.2K/30p, 4K/60p, F-Log2\",\n" +
            " \"stabilization\": \"IBIS 5 trục lên đến 7.0 stops\",\n" +
            " \"viewfinder\": \"EVF 3.69 triệu điểm, độ phóng đại 0.8x\",\n" +
            " \"screen\": \"LCD cảm ứng lật 3 hướng\",\n" +
            " \"storage\": \"2 khe SD UHS-II\",\n" +
            " \"connectivity\": \"Wi-Fi, Bluetooth, USB-C, HDMI micro, microphone/headphone qua adapter\",\n" +
            " \"battery\": \"NP-W235\",\n" +
            " \"lens_included\": \"Body only\",\n" +
            " \"weight\": \"Khoảng 557g gồm pin và thẻ nhớ\",\n" +
            " \"color\": \"Silver\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Nikon D7500 Kit AF-S DX 18-140mm VR - Đen\",\n" +
            " \"sku\": \"CAM-NIK-D7500-18140-BLK-001\",\n" +
            " \"price\": 24990000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 23,\n" +
            " \"brandId\": 12,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"type\": \"DSLR APS-C/DX\",\n" +
            " \"sensor\": \"Nikon DX format CMOS 23.5 x 15.7 mm\",\n" +
            " \"resolution\": \"20.9MP hiệu dụng\",\n" +
            " \"lens_mount\": \"Nikon F mount\",\n" +
            " \"processor\": \"EXPEED 5\",\n" +
            " \"iso_range\": \"ISO 100-51200, mở rộng tùy chế độ\",\n" +
            " \"autofocus\": \"51 điểm AF, 15 điểm cross-type\",\n" +
            " \"continuous_shooting\": \"Tối đa 8 fps\",\n" +
            " \"video\": \"4K UHD 30p, Full HD 60p\",\n" +
            " \"stabilization\": \"Ổn định hình ảnh phụ thuộc ống kính VR\",\n" +
            " \"viewfinder\": \"Optical pentaprism, khoảng 100% coverage\",\n" +
            " \"screen\": \"LCD cảm ứng nghiêng 3.2 inches, 922k dots\",\n" +
            " \"storage\": \"SD/SDHC/SDXC\",\n" +
            " \"connectivity\": \"SnapBridge, Bluetooth, Wi-Fi\",\n" +
            " \"battery\": \"EN-EL15a/EN-EL15 series\",\n" +
            " \"lens_included\": \"AF-S DX NIKKOR 18-140mm f/3.5-5.6G ED VR\",\n" +
            " \"weight\": \"Khoảng 640g body only\",\n" +
            " \"color\": \"Black\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Logitech MX Master 3S Wireless Mouse - Graphite\",\n" +
            " \"sku\": \"ACC-LOG-MXMASTER3S-GRA-001\",\n" +
            " \"price\": 2335000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 20,\n" +
            " \"brandId\": 14,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"type\": \"Chuột không dây công thái học\",\n" +
            " \"sensor\": \"Darkfield high precision sensor\",\n" +
            " \"dpi\": \"200-8000 DPI\",\n" +
            " \"buttons\": \"7 nút tùy chỉnh, cuộn MagSpeed\",\n" +
            " \"connection\": \"Bluetooth Low Energy, Logi Bolt USB Receiver tùy phiên bản\",\n" +
            " \"battery\": \"Lên đến 70 ngày sau mỗi lần sạc đầy; sạc nhanh 1 phút dùng khoảng 3 giờ\",\n" +
            " \"charging_port\": \"USB Type-C\",\n" +
            " \"compatibility\": \"Windows 10/11, macOS, Linux, ChromeOS, iPadOS tùy tính năng\",\n" +
            " \"dimensions\": \"124.9 x 84.3 x 51 mm\",\n" +
            " \"weight\": \"141g\",\n" +
            " \"color\": \"Graphite\",\n" +
            " \"features\": \"Quiet Clicks, tracking trên kính, Flow multi-device, Logi Options+\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Razer BlackWidow V4 X Green Switch - Black\",\n" +
            " \"sku\": \"ACC-RAZ-BWV4X-GREEN-BLK-001\",\n" +
            " \"price\": 2899000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 20,\n" +
            " \"brandId\": 15,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"type\": \"Bàn phím cơ gaming có dây\",\n" +
            " \"switch\": \"Razer Green Mechanical Switch, tactile and clicky\",\n" +
            " \"layout\": \"Full-size US layout\",\n" +
            " \"connection\": \"Wired USB\",\n" +
            " \"lighting\": \"Razer Chroma RGB\",\n" +
            " \"macro_keys\": \"6 phím macro chuyên dụng\",\n" +
            " \"media_control\": \"Multi-function roller và phím media phụ\",\n" +
            " \"keycaps\": \"ABS doubleshot keycaps tùy phiên bản\",\n" +
            " \"polling_rate\": \"1000Hz\",\n" +
            " \"compatibility\": \"Windows, Razer Synapse\",\n" +
            " \"color\": \"Black\",\n" +
            " \"features\": \"Gaming mode, onboard profile/hỗ trợ phần mềm tùy phiên bản\"\n" +
            " }\n" +
            " },\n" +
            " {\n" +
            " \"name\": \"Sony WH-1000XM5 Wireless Noise Cancelling - Black\",\n" +
            " \"sku\": \"ACC-SON-WH1000XM5-BLK-001\",\n" +
            " \"price\": 7490000,\n" +
            " \"stockQuantity\": 20,\n" +
            " \"categoryId\": 19,\n" +
            " \"brandId\": 10,\n" +
            " \"images\": [],\n" +
            " \"specifications\": {\n" +
            " \"type\": \"Tai nghe chụp tai không dây chống ồn\",\n" +
            " \"driver\": \"30mm dynamic driver\",\n" +
            " \"noise_cancelling\": \"Active Noise Cancelling, Auto NC Optimizer, Ambient Sound Mode\",\n" +
            " \"battery\": \"Lên đến 30 giờ khi bật chống ồn, lên đến 40 giờ khi tắt chống ồn tùy điều kiện\",\n" +
            " \"charging\": \"USB Type-C, sạc nhanh khoảng 3 phút dùng được khoảng 3 giờ tùy bộ sạc\",\n" +
            " \"connection\": \"Bluetooth 5.2, multipoint connection\",\n" +
            " \"codec\": \"SBC, AAC, LDAC\",\n" +
            " \"microphone\": \"Nhiều microphone hỗ trợ đàm thoại và chống ồn\",\n" +
            " \"weight\": \"Khoảng 250g\",\n" +
            " \"color\": \"Black\",\n" +
            " \"features\": \"Speak-to-Chat, Adaptive Sound Control, DSEE Extreme, cảm ứng điều khiển\"\n" +
            " }\n" +
            " }\n" +
            "]";
}
