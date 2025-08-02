package com.example.UserAuthService.repository;

import com.example.UserAuthService.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByValue(String value);

    Optional<Token> findByValueAndIsDeletedAndExpiryAtGreaterThan(
            String value,
            boolean deleted,
            LocalDateTime currentTime
    );
}
