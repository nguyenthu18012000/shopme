package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.common.entity.User;

@RestController
public class UserController {

	@Autowired
	private UserService service;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/users")
	public ResponseEntity<Object> listAll(Model model) {
		List<User> listUsers = service.listAll();
//		model.addAttribute("listUsers", listUsers);
		return ResponseEntity.ok(listUsers);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/users/new")
	public ResponseEntity<Object> newUser() {
		return ResponseEntity.ok("aaaaaa");
	}
}
