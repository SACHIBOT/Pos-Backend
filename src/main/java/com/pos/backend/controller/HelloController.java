package com.pos.backend.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/")
@Tag(name = "Hello Controller", description = "Controller for testing basic API endpoints")
public class HelloController {

    @GetMapping("/hello")
    @Operation(summary = "Retrieve a greeting message", description = "Returns a 'Hello World' message when called")
    public String sayHello() {
        return "Hello World";
    }

    @PostMapping("/hello")
    @Operation(summary = "Submit a greeting request", description = "Handles a POST request and responds with 'Hello World Post'")
    public String sayHelloPost() {
        return "Hello World Post";
    }

    @PutMapping("/hello")
    @Operation(summary = "Update the greeting message", description = "Handles a PUT request and responds with 'Hello World Updated'")
    public String sayHelloPut() {
        return "Hello World Updated";
    }

    @DeleteMapping("/hello")
    @Operation(summary = "Delete the greeting message", description = "Handles a DELETE request and responds with 'Hello World Deleted'")
    public String sayHelloDelete() {
        return "Hello World Deleted";
    }
}
