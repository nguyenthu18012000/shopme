package com.shopme.admin.service;

import com.shopme.admin.pojo.request.UserLoginRequest;
import com.shopme.admin.pojo.response.UserLoginResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    UserLoginResponse login(UserLoginRequest request);
}
