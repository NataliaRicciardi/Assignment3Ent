package com.cst8277.usermanagementservice;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	public UserManagementServiceApplication(UserManagementDAO userManagementDAO) {
		this.userManagementDAO = userManagementDAO;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) {
		if (userDTO.getName() == null || userDTO.getEmail() == null || userDTO.getPassword() == null) {
			return ResponseEntity.status(400).body("Invalid user data");
		}

		// Convert UserDTO to User object
		User user = new User();
		user.setId(UUID.randomUUID()); // Generate new UUID for the user
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		user.setCreated((int) (System.currentTimeMillis() / 1000)); // current timestamp

		// Add the user to the database
		if (userManagementDAO.addUser(user)) {
			return ResponseEntity.status(201).body("User registered successfully");
		} else {
			return ResponseEntity.status(500).body("Error registering user");
		}
	}

	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteUser (@RequestBody UUID userId) {
		if (userManagementDAO.deleteUser(userId)) {
			return ResponseEntity.status(200).body("User deleted successfully");
		} else {
			return ResponseEntity.status(400).body("Error deleting user");
		}
	}

	@GetMapping("/getAllUsers")
	public ResponseEntity<?> getAllUsers() {
		return ResponseEntity.status(200).body(userManagementDAO.getAllUsers());
	}

	@GetMapping("/getById")
	public ResponseEntity<?> getUserById(@RequestBody UUID userId) {
		User user = userManagementDAO.getUserById(userId);
		if (user != null) {
			return ResponseEntity.status(200).body(user);
		} else {
			return ResponseEntity.status(404).body("User not found");
		}
	}

	@GetMapping("getRoles")
	public ResponseEntity<?> getAllRoles() {
		return ResponseEntity.status(200).body(userManagementDAO.getAllRoles());
	}

	public static void main(String[] args) {
		SpringApplication.run(UserManagementServiceApplication.class, args);
	}



}
