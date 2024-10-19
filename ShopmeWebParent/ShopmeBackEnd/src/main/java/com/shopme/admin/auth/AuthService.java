package com.shopme.admin.auth;

import com.shopme.admin.pojo.request.UserLoginRequest;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    ResponseEntity<Object> login(UserLoginRequest request);
}
