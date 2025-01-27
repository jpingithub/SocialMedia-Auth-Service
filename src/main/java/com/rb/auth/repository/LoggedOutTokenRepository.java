package com.rb.auth.repository;

import com.rb.auth.entity.LoggedOutToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedOutTokenRepository extends JpaRepository<LoggedOutToken,Integer> {
}
