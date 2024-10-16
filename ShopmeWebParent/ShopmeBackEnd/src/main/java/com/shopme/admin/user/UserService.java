package com.shopme.admin.user;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	public List<User> listAll() {
		return (List<User>) this.userRepo.findAll();
	}
	
	public List<Role> listRoles() {
		return (List<Role>) this.roleRepo.findAll();
	}
	
	public void save(User user) {
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
		this.userRepo.save(user);
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

	public User get(Integer id) throws UserNotFoundException {
		try {			
			return this.userRepo.findById(id).get();
		} catch (NoSuchElementException ex) {
			throw new UserNotFoundException("Could not found any user with ID " + id);
		}
	}
}
