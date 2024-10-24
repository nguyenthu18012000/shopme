package com.shopme.admin.service;

import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.pojo.request.UserLoginRequest;
import com.shopme.admin.pojo.response.ListUserResponse;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {

    public List<User> getListUser();

    public ListUserResponse getListUserByPage(Integer pageNumber, String sortField, String sortDir, String keyword);

    public List<Role> getListRole();

    public User getUserById(Integer id) throws UserNotFoundException;

    public User createUser(User user, MultipartFile multipartFile) throws IOException;

    public User updateUser(User user, MultipartFile multipartFile) throws IOException;

    public boolean isEmailUnique(String email, Integer userId);

    public void deleteUserById(Integer id) throws UserNotFoundException;

    public ResponseEntity<Object> login(UserLoginRequest request);
}
