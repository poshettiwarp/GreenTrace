package com.greenWarrior.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.greenWarrior.enums.ActiveStatus;
import com.greenWarrior.model.Address;
import com.greenWarrior.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:/application-junit.properties")
public class AuthControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	private RequestSpecification requestSpecification;

	@BeforeEach
	private void initRequestSpecification() {
		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecification=requestSpecBuilder.setBaseUri("http://localhost:" + port + "/api/auth").build();
	}

	@Test
	public void test() {

	}

	@Test
	public void testCreateUser() {
		
		assertEquals(0, userRepository.count());  //check 1

		Address address = new Address();
		address.setStreet("1-43 chinna eklara");
		address.setCity("Madnoor");
		address.setDistrict("Kamareddy");
		address.setState("Telangana");
		address.setCountry("India");
		address.setPincode("503309");

		UserRequestDTO request = new UserRequestDTO();

	    String userName="sita_ram_01";
	    String password="12345";
		
		request.setFirstName("Sita");
		request.setLastName("Ram");
		request.setEmail("sitaram@gmail.com");
		request.setPhone("9951836251");
		request.setActive(ActiveStatus.ACTIVE);
		request.setUserName(userName);
		request.setPassword(passwordEncoder.encode(password));
		request.setAddress(address);

		// When
		ValidatableResponse validate = RestAssured.given(requestSpecification).contentType(ContentType.JSON)
				.accept(ContentType.JSON).body(request).post("/register").then();

		// Then
		assertEquals(1, userRepository.count()); // check 2

		UserResponseDto response = validate.extract().as(UserResponseDto.class);
		assertNotNull(response); // check 3
		assertEquals("sita_ram_01", response.getUserName()); // check 4

	}
}
