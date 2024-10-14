package com.pos.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pos.backend.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}