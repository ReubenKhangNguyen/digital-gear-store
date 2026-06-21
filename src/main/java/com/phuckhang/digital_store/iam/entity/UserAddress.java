package com.phuckhang.digital_store.iam.entity;


import com.phuckhang.digital_store.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "user_addresses")
public class UserAddress extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    User user;
    @Column(name = "receiver_name", nullable = false, length = 100)
    String receiverName;
    @Column(name = "receiver_phone", nullable = false, length = 20)
    String receiverPhone;
    // Dùng để tích hợp API Vận chuyển
    @Column(name = "province_id", nullable = false)
    Integer provinceId;
    @Column(name = "district_id", nullable = false)
    Integer districtId;
    @Column(name = "ward_code", nullable = false, length = 20)
    String wardCode;
    // Dùng để hiển thị lên UI (Chống cháy khi API vận chuyển sập)
    @Column(name = "province_name", nullable = false, length = 100)
    String provinceName;
    @Column(name = "district_name", nullable = false, length = 100)
    String districtName;
    @Column(name = "ward_name", nullable = false, length = 100)
    String wardName;
    @Column(name = "specific_address", nullable = false, length = 255)
    String specificAddress;
    @Column(name = "is_default", nullable = false)
    Boolean isDefault;
}
