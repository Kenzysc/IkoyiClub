package com.ikoyiclub.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ikoyiclub.web.models.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
    UserEntity findByUsername(String userName);
    UserEntity findFirstByUsername(String username);
}
