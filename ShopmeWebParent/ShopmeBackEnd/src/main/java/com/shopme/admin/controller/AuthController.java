package com.shopme.admin.controller;

import com.shopme.admin.service.AuthService;
import com.shopme.admin.pojo.request.UserLoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("auth")
@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("login")
    public ResponseEntity<Object> login(@RequestBody @Valid UserLoginRequest request) {
        return this.authService.login(request);
    }
}
