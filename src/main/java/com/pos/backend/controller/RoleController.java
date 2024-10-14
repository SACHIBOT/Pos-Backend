package com.pos.backend.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pos.backend.dto.RoleDto;
import com.pos.backend.entity.Role;
import com.pos.backend.service.RoleService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Role Controller", description = "Manages role creation, retrieval, and deletion for the POS system")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @PostMapping("/roles")
    @Operation(summary = "Create a new role", description = "Creates a new role and returns the created entity.")
    public ResponseEntity<?> createRole(@RequestBody RoleDto roleDto) {
        if (roleDto.getRoleName() == null || roleDto.getRoleName().isEmpty()) {
            return ResponseEntity.status(400).body("Please enter a valid role name");
        }

        Role role = new Role();
        role.setRoleName(roleDto.getRoleName());

        try {
            Role createdRole = roleService.createRole(role);
            return ResponseEntity.status(201).body(createdRole);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @GetMapping("/roles")
    @Operation(summary = "Retrieve all roles", description = "Returns a list of all available roles.")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.status(200).body(roles);
    }

    @DeleteMapping("/roles/{id}")
    @Operation(summary = "Delete a role by ID", description = "Deletes a role with the specified ID.")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.status(400).body("Invalid ID provided. ID must be a positive number.");
        }
        try {
            roleService.deleteRole(id);
            return ResponseEntity.status(204).build();
        } catch (Exception e) {
            return ResponseEntity.status(404).body("Role not found with ID: " + id);
        }
    }
}
