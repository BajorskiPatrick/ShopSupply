package com.bajorski.billingapp.service.impl;

import com.bajorski.billingapp.config.Role;
import com.bajorski.billingapp.entity.UserEntity;
import com.bajorski.billingapp.io.UserRequest;
import com.bajorski.billingapp.io.UserResponse;
import com.bajorski.billingapp.repository.UserRepository;
import com.bajorski.billingapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        UserEntity newUser = convertToEntity(userRequest);
        newUser = userRepository.save(newUser);
        return convertToResponse(newUser);
    }

    private UserEntity convertToEntity(UserRequest userRequest) {
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .name(userRequest.getName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(Role.getAuthorityByRole(userRequest.getRole()))
                .build();
    }

    private UserResponse convertToResponse(UserEntity newUser) {
        return UserResponse.builder()
                .userId(newUser.getUserId())
                .name(newUser.getName())
                .email(newUser.getEmail())
                .createdAt(newUser.getCreatedAt())
                .updatedAt(newUser.getUpdatedAt())
                .role(newUser.getRole())
                .build();
    }

    @Override
    public String getUserRole(String email) {
        UserEntity user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for the email: " + email));
        return user.getRole();
    }

    @Override
    public List<UserResponse> readUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Override
    public void deleteUser(String userId) {
        UserEntity user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
