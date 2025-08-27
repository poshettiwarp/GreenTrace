package com.greenWarrior.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.Optional;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.greenWarrior.dto.request.UserRequestDTO;
import com.greenWarrior.dto.response.UserResponseDto;
import com.greenWarrior.enums.ActiveStatus;
import com.greenWarrior.enums.Role;
import com.greenWarrior.exception.DuplicateUserException;
import com.greenWarrior.exception.UserNotFoundException;
import com.greenWarrior.model.User;
import com.greenWarrior.repository.UserRepository;
import com.greenWarrior.utility.UserAuth;
import com.greenWarrior.utils.TestUtils;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	public void testCreateUser_SholudCreateUser_WhenEmailUserNameAndPhoneIsUnique() {

		// Given
		User user1 = TestUtils.createUser();
		user1.setId(1L);
		user1.setUserName("sita_ram_01");
		String password = "12345";
		user1.setPassword(password);
		user1.getAddress().setId(1L);

		UserRequestDTO userRequest = new UserRequestDTO();
		userRequest.setFirstName("sita");
		userRequest.setLastName("Ram");
		userRequest.setEmail(user1.getEmail());
		userRequest.setPhone(user1.getPhone());
		userRequest.setUserName(user1.getUserName());
		userRequest.setPassword(user1.getPassword());
		userRequest.setActive(ActiveStatus.ACTIVE);
		userRequest.setAddress(user1.getAddress());

		// When
		when(userRepository.existsByEmail(anyString())).thenReturn(false);
		when(userRepository.existsByUserName(anyString())).thenReturn(false);
		when(userRepository.existsByPhone(anyString())).thenReturn(false);
		when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
		when(userRepository.save(any(User.class))).thenReturn(user1);

		// Then
		UserResponseDto savedUser = userServiceImpl.createUser(userRequest);
		assertNotNull(savedUser);
		assertEquals(1L, savedUser.getId());

		verify(userRepository, times(1)).existsByEmail(anyString());
		verify(userRepository, times(1)).existsByUserName(anyString());
		verify(userRepository, times(1)).existsByPhone(anyString());
		verify(userRepository, times(1)).save(any(User.class));

	}

	@Test
	public void testCreateUser_SholudThrowException_WhenEmailAlreadyExist() {

		// Given

		User user1 = TestUtils.createUser();
		user1.setId(1L);
		String userName = "sita_ram_01";
		String password = "12345";
		user1.setUserName(userName);
		user1.setPassword(password);
		user1.getAddress().setId(1L);

		UserRequestDTO userRequest = new UserRequestDTO();
		userRequest.setFirstName(user1.getFirstName());
		userRequest.setLastName(user1.getLastName());
		userRequest.setEmail(user1.getEmail());
		userRequest.setPhone(user1.getPhone());
		userRequest.setUserName(user1.getUserName());
		userRequest.setPassword(user1.getPassword());
		userRequest.setActive(ActiveStatus.ACTIVE);
		userRequest.setAddress(user1.getAddress());

		// When
		when(userRepository.existsByEmail(anyString())).thenReturn(true);

		assertThrows(DuplicateUserException.class, () -> userServiceImpl.createUser(userRequest));

		verify(userRepository, times(1)).existsByEmail(user1.getEmail());

	}

	@Test
	public void testCreateUser_SholudThrowException_WhenUserNameAlreadyExist() {

		// Given

		User user1 = TestUtils.createUser();
		user1.setId(1L);
		String userName = "sita_ram_01";
		String password = "12345";
		user1.setUserName(userName);
		user1.setPassword(password);
		user1.getAddress().setId(1L);

		UserRequestDTO userRequest = new UserRequestDTO();
		userRequest.setFirstName(user1.getFirstName());
		userRequest.setLastName(user1.getLastName());
		userRequest.setEmail(user1.getEmail());
		userRequest.setPhone(user1.getPhone());
		userRequest.setUserName(user1.getUserName());
		userRequest.setPassword(user1.getPassword());
		userRequest.setActive(ActiveStatus.ACTIVE);
		userRequest.setAddress(user1.getAddress());

		// When
		when(userRepository.existsByUserName(anyString())).thenReturn(true);

		assertThrows(DuplicateUserException.class, () -> userServiceImpl.createUser(userRequest));

		verify(userRepository, times(1)).existsByUserName(userName);

	}

	@Test
	public void testCreateUser_SholudThrowException_WhenPhoneAlreadyExist() {

		// Given

		User user1 = TestUtils.createUser();
		user1.setId(1L);
		String userName = "sita_ram_01";
		String password = "12345";
		user1.setUserName(userName);
		user1.setPassword(password);
		user1.getAddress().setId(1L);

		UserRequestDTO userRequest = new UserRequestDTO();
		userRequest.setFirstName(user1.getFirstName());
		userRequest.setLastName(user1.getLastName());
		userRequest.setEmail(user1.getEmail());
		userRequest.setPhone(user1.getPhone());
		userRequest.setUserName(user1.getUserName());
		userRequest.setPassword(user1.getPassword());
		userRequest.setActive(ActiveStatus.ACTIVE);
		userRequest.setAddress(user1.getAddress());

		// When
		when(userRepository.existsByPhone(anyString())).thenReturn(true);

		// Then
		assertThrows(DuplicateUserException.class, () -> userServiceImpl.createUser(userRequest));

		verify(userRepository, times(1)).existsByPhone(user1.getPhone());

	}

	@Test
	public void testGetUserById_ShouldReturnOwnRecord_WhenRoleIsUserAndIdMatches() {

		// Given

		User user = TestUtils.createUser();
		user.setId(1L);
		String userName = "sita_ram_01";
		String password = "12345";
		user.setUserName(userName);
		user.setPassword(password);
		user.getAddress().setId(1L);

		User admin = TestUtils.createUser();
		admin.setId(2L);
		admin.setRole(Role.ADMIN);
		String adminUserName = "admin";
		String adminPassword = "12345";
		admin.setUserName(adminUserName);
		admin.setPassword(adminPassword);
		admin.getAddress().setId(2L);

		// When
		when(userRepository.findByUserName("sita_ram_01")).thenReturn(user);
		when(userRepository.findByIdAndUserName(1L, "sita_ram_01")).thenReturn(Optional.of(user));

		try (MockedStatic<UserAuth> mock = Mockito.mockStatic(UserAuth.class)) {

			mock.when(() -> UserAuth.getCurrentLoggedInUser()).thenReturn("sita_ram_01");

			UserResponseDto response = userServiceImpl.getUserById(1L);

			// Then
			assertNotNull(response);
			assertEquals(1L, response.getId());
			assertEquals("sita_ram_01", response.getUserName());

			verify(userRepository, times(1)).findByIdAndUserName(1L, user.getUserName());
		}

	}

	@Test
	public void testGetUserById_ShouldReturnAnyRecord_WhenRoleIsAdminAndIdMatches() {

		// Given

		User user = TestUtils.createUser();
		user.setId(1L);
		String userName = "sita_ram_01";
		String password = "12345";
		user.setUserName(userName);
		user.setPassword(password);
		user.getAddress().setId(1L);

		User admin = TestUtils.createUser();
		admin.setId(2L);
		admin.setRole(Role.ADMIN);
		String adminUserName = "admin";
		String adminPassword = "12345";
		admin.setUserName(adminUserName);
		admin.setPassword(adminPassword);
		admin.getAddress().setId(2L);

		when(userRepository.findByUserName("admin")).thenReturn(admin);
		when(userRepository.findById(1L)).thenReturn(Optional.of(user));

		try (MockedStatic<UserAuth> mockStatic = Mockito.mockStatic(UserAuth.class)) {

			mockStatic.when(() -> UserAuth.getCurrentLoggedInUser()).thenReturn("admin");

			UserResponseDto response = userServiceImpl.getUserById(1L);

			assertNotNull(response);

			assertEquals(1L, response.getId());
			assertEquals("sita_ram_01", response.getUserName());

			verify(userRepository, times(1)).findByUserName(anyString());
			verify(userRepository, times(1)).findById(anyLong());
		}
	}

	@Test
	public void testGetUserById_ShouldThrowUserNotFoundException_WhenRoleIsAdminAndIdNotFound() {

		User admin = TestUtils.createUser();
		admin.setId(2L);
		admin.setRole(Role.ADMIN);
		String adminUserName = "admin";
		String adminPassword = "12345";
		admin.setUserName(adminUserName);
		admin.setPassword(adminPassword);
		admin.getAddress().setId(2L);

		when(userRepository.findByUserName(admin.getUserName())).thenReturn(admin);
		when(userRepository.findById(50L)).thenReturn(Optional.empty());

		try (MockedStatic<UserAuth> mockStatic = Mockito.mockStatic(UserAuth.class)) {

			mockStatic.when(() -> UserAuth.getCurrentLoggedInUser()).thenReturn(admin.getUserName());

			UserNotFoundException exception = assertThrows(UserNotFoundException.class,
					() -> userServiceImpl.getUserById(50L));

			assertEquals("User Not Found with id : 50", exception.getMessage());
			verify(userRepository, times(1)).findByUserName(any());
			verify(userRepository, times(1)).findById(anyLong());
		}

	}

	@Test
	public void testGetUserById_ShouldThrowAccessDeniedException_WhenRoleIsUserAndIdNotMatches() {

		User user1 = TestUtils.createUser();
		user1.setId(1L);
		String user1UserName = "sita_ram_01";
		String user1Password = "12345";
		user1.setUserName(user1UserName);
		user1.setPassword(user1Password);
		user1.getAddress().setId(1L);

		User user2 = TestUtils.createUser();
		user2.setId(2L);
		user2.setRole(Role.USER);
		String user2UserName = "gauri_shankar_01";
		String user2Password = "12345";
		user2.setUserName(user2UserName);
		user2.setPassword(user2Password);
		user2.getAddress().setId(2L);

		when(userRepository.findByUserName(user2.getUsername())).thenReturn(user2);
		when(userRepository.findByIdAndUserName(10L, user2UserName)).thenReturn(Optional.empty());

		try (MockedStatic<UserAuth> mockStatic = Mockito.mockStatic(UserAuth.class)) {

			mockStatic.when(() -> UserAuth.getCurrentLoggedInUser()).thenReturn(user2.getUserName());

			AccessDeniedException exception = assertThrows(AccessDeniedException.class,
					() -> userServiceImpl.getUserById(10L));

			assertEquals("Access Denied.", exception.getMessage());
			verify(userRepository, times(1)).findByUserName(anyString());
			verify(userRepository, times(1)).findByIdAndUserName(10L, user2UserName);
		}

	}

	@Test
	public void testUpdateUserById_shouldUpdateUserDetails_whenIdAndUserNameMatches() {

		User existingUser = TestUtils.createUser();
		existingUser.setId(1L);
		existingUser.setUserName("sita_ram_01");
		existingUser.setPassword("12345");
		existingUser.getAddress().setId(1L);

		User updatedUser = TestUtils.createUser();
		updatedUser.setId(1L);
		updatedUser.setFirstName("Janaki");
		updatedUser.setLastName("Ram Chandra");
		updatedUser.setEmail("sitaram123@gmail.com");
		updatedUser.setPhone("9951425342");
		updatedUser.setUserName("sita_01");

		UserRequestDTO request = new UserRequestDTO();
		request.setFirstName("Janaki");
		request.setLastName("Ram Chandra");
		request.setEmail("sitaram123@gmail.com");
		request.setPhone("9951425342");
		request.setUserName("sita_01");

		when(userRepository.findByIdAndUserName(1L, "sita_ram_01")).thenReturn(Optional.of(existingUser));
		when(userRepository.save(any(User.class))).thenReturn(updatedUser);

		UserResponseDto response = null;

		try (MockedStatic<UserAuth> mockStatic = Mockito.mockStatic(UserAuth.class)) {

			mockStatic.when(() -> UserAuth.getCurrentLoggedInUser()).thenReturn("sita_ram_01");

			response = userServiceImpl.updateUserById(1L, request);

			assertNotNull(response);
			assertEquals("Janaki", response.getFirstName());
			assertEquals("sita_01", response.getUserName());
			assertEquals(1L, response.getId());
			
			verify(userRepository,times(1)).findByIdAndUserName(1L, "sita_ram_01");
			verify(userRepository,times(1)).save(any(User.class));
			verify(userRepository,times(0)).findByIdAndUserName(1L, "sita_01");
		}

	}

}
