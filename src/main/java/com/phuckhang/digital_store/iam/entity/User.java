package com.phuckhang.digital_store.iam.entity;

import com.phuckhang.digital_store.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, unique = true, length = 50)
    String username;

    @Column(nullable = false)
    String password;

    @Column(name = "full_name", nullable = false, length = 100)
    String fullName;

    @Column(unique = true, length = 100)
    String email;

    @Column(unique = true, length = 20)
    String phone;

    String avatar;

    @Builder.Default
    @Column(name = "is_active", nullable = false)
    Boolean isActive = true;
}
