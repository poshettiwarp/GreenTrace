package com.greenWarrior.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

	private static Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<UserResponseDto> createUser(@RequestBody UserRequestDTO requestDTO) {
		LOGGER.info("Start : AuthController -------> createUser() method : " + requestDTO);
		return new ResponseEntity<UserResponseDto>(userService.createUser(requestDTO), HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<String> userLogin() {

		return ResponseEntity.ok("Login Successfull..");
	}

}
