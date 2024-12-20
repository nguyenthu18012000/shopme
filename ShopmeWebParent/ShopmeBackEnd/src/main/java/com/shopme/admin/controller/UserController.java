package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.pojo.response.ListUserResponse;
import com.shopme.admin.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl service;

    @GetMapping("/users/all")
    public ResponseEntity<Object> getListUser() {
        List<User> listUsers = service.getListUser();
        return ResponseEntity.ok(listUsers);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getListUserByPage(
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "sortField", required = false, defaultValue = "") String sortField,
            @RequestParam(value = "sortDir", required = false, defaultValue = "") String sortDir,
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword
    ) {
        ListUserResponse listUsers = service.getListUserByPage(page, sortField, sortDir, keyword);
        return ResponseEntity.ok(listUsers);
    }

    @GetMapping("/users/roles")
    public ResponseEntity<Object> getListRole() {
        List<Role> listRoles = service.getListRole();
        return ResponseEntity.ok(listRoles);
    }

    @PostMapping("/users/new")
    public String createUser(@ModelAttribute User user, @RequestPart(name = "image", required = false) MultipartFile multipartFile) throws IOException {
        boolean isEmailUnique = this.service.isEmailUnique(user.getEmail(), null);
        if (!isEmailUnique) {
            return "email is existed";
        }
        User savedUser = this.service.createUser(user, multipartFile);
        return "create user successfully";
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable(name = "id") Integer id) throws UserNotFoundException {
        try {
            User user = this.service.getUserById(id);
            return user;
        } catch (Exception e) {
            throw new UserNotFoundException("Could not found any user with ID " + id);
        }
    }

    @PutMapping("/users/edit")
    public String updateUser(@ModelAttribute User user, @RequestPart(name = "image", required = false) MultipartFile multipartFile) throws IOException {
        boolean isEmailUnique = this.service.isEmailUnique(user.getEmail(), user.getId());
        if (!isEmailUnique) {
            return "email is existed";
        }
        this.service.updateUser(user, multipartFile);
        return "update user successfully";
    }

    @DeleteMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Integer id) {
        try {
            this.service.deleteUserById(id);
            return "update user successfully";
        } catch (Exception e) {
            return "something wrong";
        }
    }

    @GetMapping("user/export/csv")
    public void exportToCSV(HttpServletResponse response) throws IOException {
        this.service.exportUserCSV(response);
    }
}
