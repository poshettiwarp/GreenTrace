package com.greenWarrior.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.greenWarrior.dto.response.UserResponseDto;
import com.greenWarrior.service.UserService;


@RestController
@RequestMapping("/api/user-search/")
public class UserSearchController {
	
	@Autowired
	private UserService userService;

	@GetMapping("/by-user-name")
	public ResponseEntity<List<UserResponseDto>> getUserByUserNameStartsWithIgnoreCase(
			@RequestParam String userName) {

		return ResponseEntity.ok(userService.getUserByUserNameStartsWithIgnoreCase(userName));
	}

	@GetMapping("/by-role")
	public ResponseEntity<List<UserResponseDto>> getUserByRole(@RequestParam String role) {
		return ResponseEntity.ok(userService.getUserByRole(role));
	}

	
	
	
}
