package com.greenWarrior.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import com.greenWarrior.dto.request.TreeRequestDTO;
import com.greenWarrior.dto.response.TreeResponseDTO;
import com.greenWarrior.enums.ActiveStatus;
import com.greenWarrior.enums.GrowthStage;
import com.greenWarrior.enums.Role;
import com.greenWarrior.model.Address;
import com.greenWarrior.model.User;
import com.greenWarrior.repository.TreeRepository;
import com.greenWarrior.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:/application-junit.properties")
public class TreeControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TreeRepository treeRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	private RequestSpecification requestSpecification;

	@PostConstruct
	public void initRequestSpecification() {
		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();

		requestSpecification = requestSpecBuilder
				.setBaseUri("http://localhost:" + port + "/api/trees").setContentType(ContentType.JSON)
				.setAccept(ContentType.JSON)/*
											 * .addHeader("Authorization", "Basic" +
											 * Base64.getEncoder().encodeToString(("sita_ram_01:12345").getBytes()))
											 */
				.build();
	}

	@BeforeEach
	public void setUp() {
		treeRepository.deleteAll();
		userRepository.deleteAll();

	}

	@Test
	public void test() {

	}

	@Test
	public void testCreateTree() {

		// Given

		assertEquals(0, userRepository.count()); // check 1

		Address address = new Address();
		address.setStreet("1-43 chinna eklara");
		address.setCity("Madnoor");
		address.setDistrict("Kamareddy");
		address.setState("Telangana");
		address.setCountry("India");
		address.setPincode("503309");

		final String userName="sita_ram_01";
		final String password="12345";
		
		User user = new User();
		user.setFirstName("Sita");
		user.setLastName("Ram");
		user.setEmail("sitaram@gmail.com");
		user.setPhone("9951836251");
		user.setActive(ActiveStatus.ACTIVE);
		user.setUserName(userName);
		user.setPassword(passwordEncoder.encode(password));
		user.setRole(Role.USER);
		user.setTreesPlanted(0);
		user.setAddress(address);
		user.setTrees(null);

		userRepository.save(user);

		assertEquals(0, treeRepository.count()); // check 2

		TreeRequestDTO treeRequest = new TreeRequestDTO();
		treeRequest.setLocalName("Mango");
		treeRequest.setSpecies("Mangifera Indica");
		treeRequest.setPlantedBy(user.getUserName());
		treeRequest.setGrowthStage(GrowthStage.SEEDLING);
		treeRequest.setLongitude(9.028);
		treeRequest.setLatitude(8.082);
		treeRequest.setNearByLocation("Near School Gate");
		

		// When
		ValidatableResponse validate = RestAssured.given().spec(requestSpecification).auth().preemptive()
				.basic(userName, password).body(treeRequest).post("/").then().statusCode(201);

		// Then
		assertEquals(1, userRepository.count()); // check 3
		assertEquals(1, treeRepository.count()); // check 4

		TreeResponseDTO response = validate.extract().as(TreeResponseDTO.class);
		assertEquals("sita_ram_01", response.getPlantedBy()); // check 5
		assertEquals("Mango", response.getLocalName()); // check 7
		assertEquals(LocalDate.now(), response.getDateOfPlantation()); // check 8
	}

}
