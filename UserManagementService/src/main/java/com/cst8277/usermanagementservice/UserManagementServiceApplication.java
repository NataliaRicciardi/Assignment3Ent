package com.cst8277.usermanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RequestMapping("/users")
@RestController
public class UserManagementServiceApplication {

	UserManagementDAO userManagementDAO;

	public UserManagementServiceApplication() {
		this.userManagementDAO = new UserManagementDAO();
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		if (userManagementDAO.addUser(user)) {
			return ResponseEntity.status(201).body("User registered successfully");
		} else {
			return ResponseEntity.status(400).body("Error registering user");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		User dbUser = userManagementDAO.getUserById(user.getUserId());
		if (dbUser != null && dbUser.getPassword().equals(user.getPassword())) {
			// Generate token
			Token token = new Token(user.getUsername());

			// Return token
			return ResponseEntity.status(200).body(token);
		} else {
			return ResponseEntity.status(401).body("Invalid username or password");
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(UserManagementServiceApplication.class, args);
	}



}
