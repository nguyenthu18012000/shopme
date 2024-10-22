package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import com.shopme.admin.exceptions.BizException;
import com.shopme.admin.pojo.request.UserLoginRequest;
import com.shopme.admin.pojo.response.BaseResponseEnum;
import com.shopme.admin.pojo.response.UserLoginResponse;
import com.shopme.admin.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtil jwtUtil;
	
	public List<User> listAll() {
		return (List<User>) this.userRepo.findAll();
	}
	
	public List<Role> listRoles() {
		return (List<Role>) this.roleRepo.findAll();
	}
	
	public User save(User user) {
		boolean isUpdatingUser = (user.getId() != null);
		if (isUpdatingUser) {
			User existedUser = this.userRepo.findById(user.getId()).get();
			if (user.getPassword().isEmpty()) {
				user.setPassword(existedUser.getPassword());
			} else {
				this.encodePassword(user);
			}
		} else {
			this.encodePassword(user);
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

	public User getUserById(Integer id) throws UserNotFoundException {
		try {			
			return this.userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not found any user with ID " + id);
		}
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
