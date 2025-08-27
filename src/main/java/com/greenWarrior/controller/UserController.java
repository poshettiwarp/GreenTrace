package com.greenWarrior.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.greenWarrior.dto.request.UserRequestDTO;
import com.greenWarrior.dto.response.UserResponseDto;
import com.greenWarrior.dto.response.UserStatisticsResponseDTO;
import com.greenWarrior.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/{id}")
	public ResponseEntity<UserResponseDto> getUserById(@PathVariable int id) {

		return ResponseEntity.ok(userService.getUserById(id));
	}

	@PatchMapping("/{id}/personal-details")
	public ResponseEntity<UserResponseDto> updateUserById(@PathVariable int id,
			@RequestBody UserRequestDTO userRequest) {
		return ResponseEntity.ok(userService.updateUserById(id, userRequest));
	}

	@GetMapping("/")
	public ResponseEntity<List<UserResponseDto>> getAllUsers() {
		return ResponseEntity.ok(userService.getAllUsers());
	}

	@PatchMapping("/{id}/address")
	public ResponseEntity<UserResponseDto> updateUserAddressByUserId(@PathVariable int id,
			@RequestBody UserRequestDTO userRequestDTO) {

		return ResponseEntity.created(null).body(userService.updateUserAddressByUserId(id, userRequestDTO));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUserById(@PathVariable int id) {

		return ResponseEntity.ok(userService.deleteUserById(id));
	}

	@PatchMapping("/{id}/status")
	public ResponseEntity<UserResponseDto> updateActiveStatus(@PathVariable int id,
			@RequestBody UserRequestDTO userRequestDTO) {
		return ResponseEntity.accepted().body(userService.updateActiveStatusById(id, userRequestDTO));
	}

	@GetMapping("/stats/{id}")
	public ResponseEntity<UserStatisticsResponseDTO> getUserStatisticsById(@PathVariable int id) {

		return ResponseEntity.ok(userService.getUserStatisticsById(id));
	}

	@PatchMapping("/{userName}/update-password")
	public ResponseEntity<UserResponseDto> updatePasswordByUserName(@PathVariable String userName,
			@RequestBody UserRequestDTO dto) {

		return ResponseEntity.ok(userService.updatePasswordByUserName(userName, dto));
	}
}
