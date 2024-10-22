package com.shopme.admin.service.impl;

import com.shopme.admin.pojo.request.UserLoginRequest;
import com.shopme.admin.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserServiceImpl userService;

    @Override
    public ResponseEntity<Object> login(UserLoginRequest request) {
        return userService.login(request);
    }
}
