package com.greenWarrior.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import com.greenWarrior.dto.request.UserRequestDTO;
import com.greenWarrior.dto.response.UserResponseDto;
import com.greenWarrior.enums.Role;
import com.greenWarrior.model.User;
import com.greenWarrior.repository.UserRepository;
import com.greenWarrior.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:/application-junit.properties")
public class UserControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private RequestSpecification requestSpecification;

	@PostConstruct
	public void initRequestSpecification() {
		RequestSpecBuilder builder = new RequestSpecBuilder();
		requestSpecification = builder.setBaseUri("http://localhost:" + port + "/api/user")
				.setContentType(ContentType.JSON).setAccept(ContentType.JSON).build();
	}

	@BeforeEach
	public void setUp() {
		userRepository.deleteAll();
	}

	@Test
	public void testGetUserById_ShouldReturnOwnDetails_WhenRoleIsUserAndIdMatches() {

		// Given
		assertEquals(0, userRepository.count()); // check 1

		String userName = "sita_ram_01";
		String password = "12345";

		User user = TestUtils.createUser();
		user.setUserName(userName);
		user.setPassword(passwordEncoder.encode(password));

		User savedUser = userRepository.save(user);

		assertEquals(1, userRepository.count()); // check 2

		// When
		ValidatableResponse validate = RestAssured.given().spec(requestSpecification).auth().preemptive()
				.basic(userName, password).get("/" + savedUser.getId()).then().statusCode(200);

		// Then
		UserResponseDto response = validate.extract().as(UserResponseDto.class);

		assertNotNull(response); // check 3
		assertEquals(savedUser.getId(), response.getId()); // check 4
		assertEquals(userName, response.getUserName()); // check 5
	}

	@Test
	public void testGetUserById_shouldReturnAnyDetails_whenRoleIsAdmin() {

		assertEquals(0, userRepository.count());

		// Given

		User admin = TestUtils.createUser();
		String userName = "admin";
		String password = "12345";
		admin.setUserName(userName);
		admin.setPassword(passwordEncoder.encode(password));
		admin.setRole(Role.ADMIN);

		userRepository.save(admin);

		User user1 = TestUtils.createUser();
		user1.setUserName("gauri");
		user1.setPassword(passwordEncoder.encode("gauri@123"));

		User savedUser1 = userRepository.save(user1);

		User user2 = TestUtils.createUser();
		user2.setUserName("sita");
		user2.setPassword(passwordEncoder.encode("sita@123"));

		userRepository.save(user2);

		assertEquals(3, userRepository.count());

		// When

		ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification).auth().preemptive()
				.basic(userName, password).get("/" + savedUser1.getId()).then().statusCode(200);

		// Then
		UserResponseDto response = validatableResponse.extract().as(UserResponseDto.class);
		assertNotNull(response);
		assertEquals("gauri", response.getUserName());
		assertEquals(Role.USER, response.getRole());
	}

	@Test
	public void testGetUserById_statusCodeShould403_whenRoleIsUserAndIdMismatchs() {

		assertEquals(0, userRepository.count());

		// Given

		User admin = TestUtils.createUser();
		String userName = "admin";
		String password = "12345";
		admin.setUserName(userName);
		admin.setPassword(passwordEncoder.encode(password));
		admin.setRole(Role.ADMIN);

		userRepository.save(admin);

		User user1 = TestUtils.createUser();
		String user1UserName = "sita";
		String user1Password = "12345";
		user1.setUserName(user1UserName);
		user1.setPassword(passwordEncoder.encode(user1Password));

		userRepository.save(user1);

		User user2 = TestUtils.createUser();
		String user2UserName = "gauri";
		String user2Password = "12345";
		user2.setUserName(user2UserName);
		user2.setPassword(passwordEncoder.encode(user2Password));

		User savedUser2 = userRepository.save(user2);

		assertEquals(3, userRepository.count());

		// When
		ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification).auth().preemptive()
				.basic(user1UserName, user1Password).get("/" + savedUser2.getId()).then().statusCode(403);

		// Then

		validatableResponse.body("code", equalTo("403 FORBIDDEN"), "message", equalTo("Access Denied."));
	}

	@Test
	public void testGetUserById_statusCodeShould404_whenRoleIsAdminAndUserWithIdNotFound() {

		assertEquals(0, userRepository.count());

		// Given

		User admin = TestUtils.createUser();
		String userName = "admin";
		String password = "12345";
		admin.setUserName(userName);
		admin.setPassword(passwordEncoder.encode(password));
		admin.setRole(Role.ADMIN);

		userRepository.save(admin);

		User user1 = TestUtils.createUser();
		String user1UserName = "sita";
		String user1Password = "12345";
		user1.setUserName(user1UserName);
		user1.setPassword(passwordEncoder.encode(user1Password));

		userRepository.save(user1);

		User user2 = TestUtils.createUser();
		String user2UserName = "gauri";
		String user2Password = "12345";
		user2.setUserName(user2UserName);
		user2.setPassword(passwordEncoder.encode(user2Password));

		userRepository.save(user2);

		assertEquals(3, userRepository.count());

		// When
		ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification).auth().preemptive()
				.basic(userName, password).get("/" + 1097).then().statusCode(404);

		// Then
		validatableResponse.body("code", equalTo("404 NOT_FOUND"), "message", equalTo("User Not Found with id : 1097"));

	}

	@Test
	public void testUpdateUserById_shouldUpdateUserDetails_whenIdAndUserNameMatches() {

		// Given

		assertEquals(0, userRepository.count());

		User user = TestUtils.createUser();
		user.setUserName("sita_ram_01");
		user.setPassword(passwordEncoder.encode("12345"));

		User existingUser = userRepository.save(user);

		assertEquals(1, userRepository.count());

		UserRequestDTO request = new UserRequestDTO();
		request.setFirstName("Janaki");
		request.setLastName("Ram Chandra");
		request.setEmail("sitaram123@gmail.com");
		request.setPhone("9951425342");
		request.setUserName("sita_01");

		// When
		ValidatableResponse validatableResponse = RestAssured.given().spec(requestSpecification).auth().preemptive()
				.basic("sita_ram_01", "12345").body(request).patch("/" + existingUser.getId() + "/personal-details")
				.then().statusCode(200);

		// Then

		UserResponseDto response = validatableResponse.extract().as(UserResponseDto.class);

		assertNotNull(response);
		assertEquals("sita_01", response.getUserName());
		assertEquals("sitaram123@gmail.com", response.getEmail());
		assertEquals("9951425342", response.getPhone());

	}

}
