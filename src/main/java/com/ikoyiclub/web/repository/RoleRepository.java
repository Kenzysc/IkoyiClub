package com.ikoyiclub.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ikoyiclub.web.models.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
