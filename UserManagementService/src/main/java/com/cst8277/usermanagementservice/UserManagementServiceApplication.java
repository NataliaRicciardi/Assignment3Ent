package com.cst8277.usermanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser (@RequestBody UUID user_id) {
		return ResponseEntity.status(200).body("");
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers() {
		return ResponseEntity.status(200).body(userManagementDAO.getAllUsers());
	}

	@GetMapping("/getById")
	public ResponseEntity<?> getUserById(@RequestBody UUID user_id) {
		return ResponseEntity.status(200).body("");
	}

	@GetMapping("getRoles")
	public ResponseEntity<?> getAllRoles() {
		return ResponseEntity.status(200).body("");
	}

	public static void main(String[] args) {
		SpringApplication.run(UserManagementServiceApplication.class, args);
	}



}
