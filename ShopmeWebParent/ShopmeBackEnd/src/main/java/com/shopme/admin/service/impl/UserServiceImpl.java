package com.shopme.admin.service.impl;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import com.shopme.admin.util.FileUploadUtil;
import com.shopme.admin.exception.BizException;
import com.shopme.admin.exception.UserNotFoundException;
import com.shopme.admin.pojo.request.UserLoginRequest;
import com.shopme.admin.pojo.response.BaseResponseEnum;
import com.shopme.admin.pojo.response.UserLoginResponse;
import com.shopme.admin.repository.RoleRepository;
import com.shopme.admin.repository.UserRepository;
import com.shopme.admin.security.JwtUtil;
import com.shopme.admin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;
	
	public List<User> getListUser() {
		return (List<User>) this.userRepo.findAll();
	}
	
	public List<Role> getListRole() {
		return (List<Role>) this.roleRepo.findAll();
	}

	public User getUserById(Integer id) throws UserNotFoundException {
		try {
			return this.userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not found any user with ID " + id);
		}
	}

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
	
	public boolean isEmailUnique(String email, Integer userId) {
		User userByEmail = userRepo.getUserByEmail(email);
		if (userId == null) {			
			return userByEmail == null;
		}
		return userId == userByEmail.getId();
	}

	public void deleteUserById(Integer id) throws UserNotFoundException {
		Long countById = this.userRepo.countById(id);
		if (countById == null || countById == 0) {
			throw new UserNotFoundException("Could not found any user with ID " + id);
		}
		this.userRepo.deleteById(id);
	}

	public ResponseEntity<Object> login(UserLoginRequest request) {
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
		return ResponseEntity.ok(response);
	}
}
