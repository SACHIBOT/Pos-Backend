package com.pos.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pos.backend.dto.NewUserDto;
import com.pos.backend.dto.UserDto;
import com.pos.backend.entity.Role;
import com.pos.backend.entity.User;
import com.pos.backend.security.JwtUtils;
import com.pos.backend.service.RoleService;
import com.pos.backend.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@Tag(name = "Auth Controller", description = "Endpoints for user authentication.")
public class AuthController {

        @Autowired
        private AuthenticationManager authenticationManager;

        @Autowired
        private JwtUtils jwtUtils;

        @Autowired
        private UserService userService;

        @Autowired
        private RoleService roleService;

        @Operation(summary = "User info", description = "Retrives authenticated user info.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User details found"),
                        @ApiResponse(responseCode = "400", description = "No authentication or invalid username"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/user")
        public ResponseEntity<?> getUserInfo() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                if (authentication != null && authentication.isAuthenticated()) {

                        String username = authentication.getName();
                        User user = userService.getUserByUserName(username);
                        if (user == null) {
                                return ResponseEntity.status(400).body("No user found");
                        }
                        return ResponseEntity.status(200).body(user);
                } else {
                        return ResponseEntity.status(400).body("No authenticated user found");

                }
        }

        @Operation(summary = "User login", description = "Authenticates the user and returns a JWT token.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
                        @ApiResponse(responseCode = "401", description = "Invalid username or password"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PostMapping("/auth/login")
        public ResponseEntity<?> login(@RequestBody UserDto userDto) {
                User user = userService.getUserByUserName(userDto.getUsername());
                if (user == null) {
                        return ResponseEntity.status(400).body("User not found");

                }

                Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
                                                userDto.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwtToken = jwtUtils.generateJwtToken(authentication);
                return ResponseEntity.status(200).body(jwtToken);

        }

        @Operation(summary = "Create user", description = "Allows an admin to create a new user with the specified role.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User successfully created"),
                        @ApiResponse(responseCode = "400", description = "Invalid input or role not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PostMapping("/admin/users")
        public ResponseEntity<?> createUser(@RequestBody NewUserDto userDto) {
                User user = new User();
                user.setUsername(userDto.getUsername());
                user.setPassword(userDto.getPassword());

                Role role = roleService.getRoleById(userDto.getRoleId());

                if (role == null) {

                        return ResponseEntity.status(400).body("Role not found");
                }
                user.setRole(role);
                return ResponseEntity.status(200).body(userService.createUser(user));
        }

        @Operation(summary = "Get all users", description = "Retrieves a list of all users.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @GetMapping("/admin/allusers")
        public ResponseEntity<?> getAllUsers() {
                try {
                        List<User> users = userService.getAllUsers();
                        return ResponseEntity.status(200).body(users);
                } catch (Exception e) {
                        return ResponseEntity.status(500)
                                        .body("Failed to retrieve users.");
                }
        }

        @DeleteMapping("/admin/users/{username}")
        @Operation(summary = "Delete a user", description = "Deletes a user based on its username.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "user successfully deleted"),
                        @ApiResponse(responseCode = "404", description = "user not found"),
                        @ApiResponse(responseCode = "403", description = "Can'r remove Admin"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        public ResponseEntity<?> deleteUser(@PathVariable String username) {
                User existingUser = userService.getUserByUserName(username);
                if (existingUser == null) {
                        return ResponseEntity.status(404).body("user not found");
                }
                String userRole = existingUser.getRole().getRoleName().toUpperCase();
                if (userRole.equals("ADMIN")) {
                        return ResponseEntity.status(403).body("Can't remove admin");

                }

                try {
                        userService.deleteUser(existingUser.getUserId());
                        return ResponseEntity.status(200).build();
                } catch (Exception e) {
                        return ResponseEntity.status(500).body(e.getMessage());
                }
        }

        @Operation(summary = "Update user role", description = "Updates the role of a specified user.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User role updated successfully"),
                        @ApiResponse(responseCode = "404", description = "User or role not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PutMapping("/admin/users/{userId}/role/{roleId}")
        public ResponseEntity<?> updateUserRole(@PathVariable Long userId, @PathVariable Long roleId) {
                User updatedUser = userService.updateUserRole(userId, roleId);
                if (updatedUser == null) {
                        return ResponseEntity.status(404).body("User or role not found");
                }
                return ResponseEntity.status(200).body(updatedUser);
        }

}
