package com.greenWarrior.serviceImpl;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.greenWarrior.dto.request.UserRequestDTO;
import com.greenWarrior.dto.response.UserResponseDto;
import com.greenWarrior.dto.response.UserStatisticsResponseDTO;
import com.greenWarrior.enums.ErrorCodes;
import com.greenWarrior.enums.Role;
import com.greenWarrior.exception.DuplicateUserException;
import com.greenWarrior.exception.InvalidRequestException;
import com.greenWarrior.exception.UserNotFoundException;
import com.greenWarrior.mapper.UserMapper;
import com.greenWarrior.model.Address;
import com.greenWarrior.model.User;
import com.greenWarrior.repository.UserRepository;
import com.greenWarrior.service.UserService;
import com.greenWarrior.utility.UserAuth;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	// Public Access

	@Override
	public UserResponseDto createUser(UserRequestDTO requestDTO) {

		if (userRepository.existsByEmail(requestDTO.getEmail()) || userRepository.existsByPhone(requestDTO.getPhone())
				|| userRepository.existsByUserName(requestDTO.getUserName())) {
			throw new DuplicateUserException(ErrorCodes.DUPLICATE_USER.getCode(),
					ErrorCodes.DUPLICATE_USER.getMessage());
		} else {
			User user = UserMapper.toEntity(requestDTO);

			String encodedPassword = passwordEncoder.encode(requestDTO.getPassword());
			user.setPassword(encodedPassword);
			User savedUser = userRepository.save(user);
			UserResponseDto userResponse = UserMapper.getUserResponse(savedUser);
			return userResponse;
		}
	}

	// Admin & Specific User Access

	@Override
	public UserResponseDto getUserById(long id) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User currentUser = userRepository.findByUserName(loggedInUser);

		if (currentUser.getRole().equals(Role.ADMIN)) {

			Optional<User> existUser = userRepository.findById(id);

			if (existUser.isPresent()) {
				User user = existUser.get();
				return UserMapper.getUserResponse(user);
			} else {
				throw new UserNotFoundException(	ErrorCodes.USER_NOT_FOUND.getCode(),
						ErrorCodes.USER_NOT_FOUND.getMessage() + " with id : " + id);
			}

		} else if (currentUser.getRole().equals(Role.USER)) {
			Optional<User> existUser = userRepository.findByIdAndUserName(id, loggedInUser);

			if (existUser.isPresent()) {
				User user = existUser.get();
				return UserMapper.getUserResponse(user);
			} else {
				throw new AccessDeniedException(ErrorCodes.ACCESS_DENIED_EXCEPTION.getMessage());
			}
		}

		throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
				ErrorCodes.INVALID_REQUEST.getMessage());
	}

	// Only User Access

	@Override
	public UserResponseDto updateUserById(long id, UserRequestDTO userRequest) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		Optional<User> existingUser = userRepository.findByIdAndUserName(id, loggedInUser);

		if (existingUser.isPresent()) {

			User user = existingUser.get();

			if (userRequest.getFirstName() != null) {
				user.setFirstName(userRequest.getFirstName());
			}

			if (userRequest.getLastName() != null) {
				user.setLastName(userRequest.getLastName());
			}

			if (userRequest.getEmail() != null) {

				if (userRequest.getEmail().equalsIgnoreCase(user.getEmail())) {
					throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
							ErrorCodes.INVALID_REQUEST.getMessage() + " - Email is Associated with Current Account.");
				} else if (userRepository.existsByEmail(userRequest.getEmail())) {
					throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
							ErrorCodes.INVALID_REQUEST.getMessage() + " - Email is Associated with Another Account.");
				} else {
					user.setEmail(userRequest.getEmail());
				}

			}

			if (userRequest.getPhone() != null) {
				if (userRequest.getPhone().equalsIgnoreCase(user.getPhone())) {
					throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
							ErrorCodes.INVALID_REQUEST.getMessage()
									+ " Phone Number is Associated with Current Account.");
				} else if (userRepository.existsByPhone(userRequest.getPhone())) {

					throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
							ErrorCodes.INVALID_REQUEST.getMessage()
									+ " Phone Number is Associated with Another Account.");
				} else {
					user.setPhone(userRequest.getPhone());
				}
			}

			if (userRequest.getUserName() != null) {

				if (userRequest.getUserName().equalsIgnoreCase(user.getUserName())) {
					throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
							ErrorCodes.INVALID_REQUEST.getMessage() + " User Name is Associated with Current Account.");
				} else if (userRepository.existsByUserName(userRequest.getUserName())) {
					throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
							ErrorCodes.INVALID_REQUEST.getMessage() + " User Name Not Available.");
				} else {
					user.setUserName(userRequest.getUserName());
				}

			}

			return UserMapper.getUserResponse(userRepository.save(user));

		} else {
			throw new AccessDeniedException("Access Denied.");
		}

	}

	// Only User Access
	public UserResponseDto updateUserAddressByUserId(long id, UserRequestDTO userRequestDTO) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		Optional<User> user = userRepository.findByIdAndUserName(id, loggedInUser);

		if (user.isPresent()) {

			User savedUser = user.get();

			Address address = savedUser.getAddress();
			System.out.println(address);

			if (userRequestDTO.getAddress().getStreet() != null) {
				address.setStreet(userRequestDTO.getAddress().getStreet());
			}

			if (userRequestDTO.getAddress().getCity() != null) {
				address.setCity(userRequestDTO.getAddress().getCity());
			}

			if (userRequestDTO.getAddress().getDistrict() != null) {
				address.setDistrict(userRequestDTO.getAddress().getDistrict());
			}

			if (userRequestDTO.getAddress().getState() != null) {
				address.setState(userRequestDTO.getAddress().getState());
			}

			if (userRequestDTO.getAddress().getCountry() != null) {
				address.setCountry(userRequestDTO.getAddress().getCountry());
			}

			if (userRequestDTO.getAddress().getPincode() != null) {
				address.setPincode(userRequestDTO.getAddress().getPincode());
			}

			savedUser.setAddress(address);

			return UserMapper.getUserResponse(userRepository.save(savedUser));

		} else {
			throw new AccessDeniedException("Access Denied.");
		}

	}

	// Only Admin Access
	@Override
	public List<UserResponseDto> getAllUsers() {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User currentUser = userRepository.findByUserName(loggedInUser);

		List<UserResponseDto> listOfUsers = null;

		if (currentUser.getRole().equals(Role.ADMIN)) {
			List<User> users = userRepository.findAll();
			if (!users.isEmpty()) {
				listOfUsers = UserMapper.getAllUsers(users);
			} else {
				throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND.getCode(),
						ErrorCodes.USER_NOT_FOUND.getMessage() + " - no users found.");
			}

			return listOfUsers;
		} else {
			throw new AccessDeniedException("Access Denied.");
		}
	}

	// Only Admin Access

	@Override
	public List<UserResponseDto> getUserByRole(String role) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User currentUser = userRepository.findByUserName(loggedInUser);

		if (currentUser.getRole().equals(Role.ADMIN)) {
			List<User> users = userRepository.findByRole(role);

			if (!users.isEmpty()) {

				return UserMapper.getAllUsers(users);

			} else {
				throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND.getCode(),
						ErrorCodes.USER_NOT_FOUND.getMessage() + " - with role " + role);
			}
		} else {
			throw new AccessDeniedException("Access Denied.");
		}

	}

	// Both Admin and User

	@Override
	public String deleteUserById(long id) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User user = userRepository.findByUserName(loggedInUser);

		if (user.getRole().equals(Role.ADMIN)) {
			Optional<User> existingUser = userRepository.findById(id);
			if (existingUser.isPresent()) {
				User savedUser = existingUser.get();
				userRepository.deleteById(savedUser.getId());

				return "User Deleted Successfully With Id - " + id + ".";
			} else {
				throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND.getCode(),
						ErrorCodes.USER_NOT_FOUND.getMessage() + " - with id " + id);
			}
		} else if (user.getRole().equals(Role.USER)) {
			Optional<User> existingUser = userRepository.findByIdAndUserName(id, loggedInUser);
			if (existingUser.isPresent()) {
				User savedUser = existingUser.get();
				userRepository.deleteById(savedUser.getId());

				return "You Have Deleted Your Account Successfully.";
			} else {
				throw new AccessDeniedException("Access Denied Cannot Perfom Action.");
			}
		} else {
			throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
					ErrorCodes.INVALID_REQUEST.getMessage() + " - Active Status Not Updated.");
		}
	}

	// Only Admin Access
	@Override
	public List<UserResponseDto> getUserByUserNameStartsWithIgnoreCase(String userName) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User currentUser = userRepository.findByUserName(loggedInUser);

		if (currentUser.getRole().equals(Role.ADMIN)) {
			List<User> users = userRepository.findByUserNameStartsWithIgnoreCase(userName);

			if (!users.isEmpty()) {

				return UserMapper.getAllUsers(users);

			} else {
				throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND.getCode(),
						ErrorCodes.USER_NOT_FOUND.getMessage() + " - with user name - " + userName);
			}
		} else {
			throw new AccessDeniedException("Access Denied.");
		}

	}

	// Both Admin And User

	@Override
	public UserResponseDto updateActiveStatusById(long id, UserRequestDTO userRequestDTO) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		Optional<User> user = userRepository.findByIdAndUserName(id, loggedInUser);

		if (user.isPresent()) {

			User savedUser = user.get();

			if (userRequestDTO.getActive() != null) {
				savedUser.setActive(userRequestDTO.getActive());
			} else {
				throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
						ErrorCodes.INVALID_REQUEST.getMessage() + " - Active Status Not Updated.");
			}

			return UserMapper.getUserResponse(userRepository.save(savedUser));

		} else {
			throw new AccessDeniedException("Access Denied.");
		}

	}

	// Both Admin and User
	@Override
	public UserResponseDto updatePasswordByUserName(String userName, UserRequestDTO userRequestDTO) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User currentUser = userRepository.findByUserName(loggedInUser);

		Optional<User> user = userRepository.findByIdAndUserName(currentUser.getId(), userName);

		if (user.isPresent()) {

			User savedUser = user.get();

			if (userRequestDTO.getPassword() != null) {
				savedUser.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
				return UserMapper.getUserResponse(userRepository.save(savedUser));
			} else {
				throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
						ErrorCodes.INVALID_REQUEST.getMessage() + " - Password Not Updated.");
			}

		} else {
			throw new AccessDeniedException("Access Denied.");
		}
	}

	// Both Admin and User Access

	@Override
	public UserStatisticsResponseDTO getUserStatisticsById(long id) {

		String loggedInUser = UserAuth.getCurrentLoggedInUser();

		User currentUser = userRepository.findByUserName(loggedInUser);

		if (currentUser.getRole().equals(Role.ADMIN)) {

			Optional<User> user = userRepository.findById(id);

			if (user.isPresent()) {

				User existingUser = user.get();

				UserStatisticsResponseDTO userStatistics = UserMapper.getUserStatistics(existingUser);
				userStatistics.setUserName(existingUser.getUserName());
				return userStatistics;

			} else {
				throw new UserNotFoundException(ErrorCodes.USER_NOT_FOUND.getCode(),
						ErrorCodes.USER_NOT_FOUND.getMessage() + " - with id - " + id);
			}

		} else if (currentUser.getRole().equals(Role.USER)) {

			Optional<User> user = userRepository.findByIdAndUserName(currentUser.getId(), loggedInUser);

			if (user.isPresent()) {

				User existingUser = user.get();
				UserStatisticsResponseDTO userStatistics = UserMapper.getUserStatistics(existingUser);
				userStatistics.setUserName(existingUser.getUserName());
				return userStatistics;

			} else {
				throw new AccessDeniedException("Access Denied.");
			}
		} else {
			throw new InvalidRequestException(ErrorCodes.INVALID_REQUEST.getCode(),
					ErrorCodes.INVALID_REQUEST.getMessage() + " - Password Not Updated.");
		}

	}

}
