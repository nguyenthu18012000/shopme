package com.shopme.admin.service.impl;

import com.shopme.admin.pojo.request.UserLoginRequest;
import com.shopme.admin.pojo.response.UserLoginResponse;
import com.shopme.admin.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserServiceImpl userService;

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        return this.userService.login(request);
    }
}
