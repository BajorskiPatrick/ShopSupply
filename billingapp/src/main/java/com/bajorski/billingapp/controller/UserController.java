package com.bajorski.billingapp.controller;

import com.bajorski.billingapp.io.UserRequest;
import com.bajorski.billingapp.io.UserResponse;
import com.bajorski.billingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserRequest userRequest) {
        try {
            System.out.println("Created use");

            UserResponse newUser = userService.createUser(userRequest);

            System.out.println("Created user: " + newUser.getEmail());

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(newUser.getUserId())
                    .toUri();

            return ResponseEntity.created(location).body(newUser);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error while creating user: " + e.getMessage());
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> fetchUsers() {
        List<UserResponse> users = userService.readUsers();

        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> removeUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
