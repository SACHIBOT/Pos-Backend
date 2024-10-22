package com.pos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

        @Operation(summary = "User login", description = "Authenticates the user and returns a JWT token.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Login successful, JWT token returned"),
                        @ApiResponse(responseCode = "401", description = "Invalid username or password"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PostMapping("/auth/login")
        public String login(@RequestBody UserDto userDto) {
                User user = userService.getUserByUserName(userDto.getUsername());
                if (user == null) {
                        return "user not found";
                }

                Authentication authentication = authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
                                                userDto.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwtToken = jwtUtils.generateJwtToken(authentication);
                return jwtToken;
        }

        @Operation(summary = "Create user", description = "Allows an admin to create a new user with the specified role.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "User successfully created"),
                        @ApiResponse(responseCode = "400", description = "Invalid input or role not found"),
                        @ApiResponse(responseCode = "500", description = "Internal server error")
        })
        @PostMapping("/admin/users")
        public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
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
}
