package com.shopme.admin.controller;

import com.shopme.admin.pojo.response.UserLoginResponse;
import com.shopme.admin.service.AuthService;
import com.shopme.admin.pojo.request.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("login")
    public UserLoginResponse login(@RequestBody @Valid UserLoginRequest request) {
        return this.authService.login(request);
    }
}
