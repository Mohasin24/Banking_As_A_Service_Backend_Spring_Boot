package com.authentication_service.repository;

import com.authentication_service.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuthRepo extends JpaRepository<Auth, Long> {

    Auth findUserByUsername(String username);
}
