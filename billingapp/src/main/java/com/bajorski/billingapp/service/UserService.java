package com.bajorski.billingapp.service;

import com.bajorski.billingapp.io.UserRequest;
import com.bajorski.billingapp.io.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest userRequest);

    String getUserRole(String email);

    List<UserResponse> readUsers();

    void deleteUser(String userId);
}
