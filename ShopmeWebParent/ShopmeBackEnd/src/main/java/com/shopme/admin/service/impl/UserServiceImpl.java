package com.shopme.admin.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import com.shopme.admin.constants.CommonConstant;
import com.shopme.admin.pojo.response.ListUserResponse;
import com.shopme.admin.util.FileUploadUtil;
import com.shopme.admin.exception.BizException;
import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.pojo.request.UserLoginRequest;
import com.shopme.admin.pojo.response.BaseResponseEnum;
import com.shopme.admin.pojo.response.UserLoginResponse;
import com.shopme.admin.repository.RoleRepository;
import com.shopme.admin.repository.UserRepository;
import com.shopme.admin.config.JwtUtil;
import com.shopme.admin.service.UserService;
import com.shopme.admin.util.UserCsvExporter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;

    private final RoleRepository roleRepo;

    private final PasswordEncoder passwordEncoder;

    private final JwtUtil jwtUtil;

    public List<User> getListUser() {
        return (List<User>) this.userRepo.findAll();
    }

    @Override
    public ListUserResponse getListUserByPage(Integer pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, CommonConstant.USER_PER_PAGE, sort);

        Page<User> page;
        if (keyword != "") {
            page = this.userRepo.findAll(keyword, pageable);
        } else {
            page = this.userRepo.findAll(pageable);
        }


        ListUserResponse listUserResponse = new ListUserResponse();
        listUserResponse.setItems(page.getContent());
        listUserResponse.setPage(pageNumber);
        listUserResponse.setTotalPage(page.getTotalPages());
        listUserResponse.setPageSize(page.getSize());
        listUserResponse.setTotalItems(page.getTotalElements());
        return listUserResponse;
    }

    @Override
    public List<Role> getListRole() {
        return (List<Role>) this.roleRepo.findAll();
    }

    @Override
    public User getUserById(Integer id) throws UserNotFoundException {
        try {
            return this.userRepo.findById(id).get();
        } catch (NoSuchElementException ex) {
            throw new UserNotFoundException("Could not found any user with ID " + id);
        }
    }

    @Override
    public User createUser(User user, MultipartFile multipartFile) throws IOException {
        this.encodePassword(user);
        User savedUser = this.userRepo.save(user);
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            savedUser.setPhotos(fileName);
            String uploadDir = "user-photo/" + savedUser.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return this.userRepo.save(user);
    }

    @Override
    public User updateUser(User user, MultipartFile multipartFile) throws IOException {
        User existedUser = this.userRepo.findById(user.getId()).get();
        if (user.getPassword().isEmpty()) {
            user.setPassword(existedUser.getPassword());
        } else {
            this.encodePassword(user);
        }
        if (multipartFile != null) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            user.setPhotos(fileName);
            String uploadDir = "user-photo/" + user.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return this.userRepo.save(user);
    }

    private void encodePassword(User user) {
        String encodedPassword = this.passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
    }

    @Override
    public boolean isEmailUnique(String email, Integer userId) {
        User userByEmail = userRepo.getUserByEmail(email);
        System.out.println(userByEmail);
        if (userByEmail == null) return true;
        if (userId == null) {
            return false;
        }
        return userId == userByEmail.getId();
    }

    @Override
    public void deleteUserById(Integer id) throws UserNotFoundException {
        Long countById = this.userRepo.countById(id);
        if (countById == null || countById == 0) {
            throw new UserNotFoundException("Could not found any user with ID " + id);
        }
        this.userRepo.deleteById(id);
    }

    @Override
    public UserLoginResponse login(UserLoginRequest request) {
        User userEntity = this.userRepo.getUserByEmail(request.getEmail());
//				.orElseThrow(() -> new BizException(BaseResponseEnum.BAD_REQUEST, "Invalid credentials"));

        if (userEntity == null) {
            throw new BizException(BaseResponseEnum.BAD_REQUEST, "Invalid credentials");
        }

        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())) {
            throw new BizException(BaseResponseEnum.UN_AUTHORIZE, "Invalid credentials");
        }

        UserLoginResponse response = new UserLoginResponse();
        response.setAccessToken(jwtUtil.generateLoginJwtToken(userEntity));
        response.setUserId(userEntity.getId());
        return response;
    }

    @Override
    public void exportUserCSV(HttpServletResponse response) {
        List<User> listUsers = this.getListUser();
        UserCsvExporter exporter = new UserCsvExporter();
        try {
            exporter.export(listUsers, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
