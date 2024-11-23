package com.cst8277.usermanagementservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RequestMapping("/users")
@RestController
public class UserManagementServiceApplication {

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@RequestBody User user) {
		return null;
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		return null;
	}

	public static void main(String[] args) {
		SpringApplication.run(UserManagementServiceApplication.class, args);
	}



}
