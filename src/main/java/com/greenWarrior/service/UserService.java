package com.greenWarrior.service;

import java.util.List;

import com.greenWarrior.dto.request.UserRequestDTO;
import com.greenWarrior.dto.response.UserResponseDto;
import com.greenWarrior.dto.response.UserStatisticsResponseDTO;

public interface UserService {

	UserResponseDto createUser(UserRequestDTO requestDTO);

	UserResponseDto getUserById(long id);

	List<UserResponseDto> getAllUsers();

	UserResponseDto updateUserById(long id, UserRequestDTO userRequest);

	UserResponseDto updateUserAddressByUserId(long id, UserRequestDTO userRequest);

	String deleteUserById(long id);

	List<UserResponseDto> getUserByRole(String role);

	List<UserResponseDto> getUserByUserNameStartsWithIgnoreCase(String userName);

	UserResponseDto updateActiveStatusById(long id, UserRequestDTO userRequestDTO);

	UserStatisticsResponseDTO getUserStatisticsById(long id);

	UserResponseDto updatePasswordByUserName(String userName, UserRequestDTO userRequestDTO);
}
