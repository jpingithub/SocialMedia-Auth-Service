package com.rb.auth.repository;

import com.rb.auth.entity.LoggedOutToken;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoggedOutTokenRepository extends JpaRepository<LoggedOutToken,Integer> {
    Optional<LoggedOutToken> findByToken(String token);
    @Transactional
    void deleteByExpiresAtLessThan(Long now);
}
