package com.phuckhang.digital_store.iam.repository;

import com.phuckhang.digital_store.iam.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
