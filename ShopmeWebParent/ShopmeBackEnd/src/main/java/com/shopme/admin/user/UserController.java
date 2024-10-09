package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@RestController
public class UserController {

	@Autowired
	private UserService service;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/users")
	public ResponseEntity<Object> listAll() {
		List<User> listUsers = service.listAll();
		return ResponseEntity.ok(listUsers);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/users/roles")
	public ResponseEntity<Object> listRoles() {
		List<Role> listRoles = service.listRoles();
		return ResponseEntity.ok(listRoles);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping("/users/new")
	public String newUser(@RequestBody User user) {
		boolean isEmailUnique =  this.service.isEmailUnique(user.getEmail());
		if (!isEmailUnique) {
			return "email is existed";
		}
		this.service.save(user);
		return "create user successfully";
	}
}
