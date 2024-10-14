package com.pos.backend.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.pos.backend.entity.Role;

@Service
public interface RoleService {

    Role createRole(Role role);

    List<Role> getAllRoles();

    Role getRoleById(Long id);

    void deleteRole(Long id);
}
