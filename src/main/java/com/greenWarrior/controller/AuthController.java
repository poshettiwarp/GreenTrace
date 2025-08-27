package com.greenWarrior.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenWarrior.dto.request.UserRequestDTO;
import com.greenWarrior.dto.response.UserResponseDto;
import com.greenWarrior.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDTO requestDTO) {

		return ResponseEntity.created(null).body(userService.createUser(requestDTO));
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> userLogin() {

		return ResponseEntity.ok("Login Successfull..");
	}
	
	
}
