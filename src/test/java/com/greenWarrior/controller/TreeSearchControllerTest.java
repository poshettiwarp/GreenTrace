package com.greenWarrior.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import com.greenWarrior.dto.response.TreeResponseDTO;
import com.greenWarrior.enums.GrowthStage;
import com.greenWarrior.enums.Role;
import com.greenWarrior.model.Tree;
import com.greenWarrior.model.User;
import com.greenWarrior.repository.TreeRepository;
import com.greenWarrior.repository.UserRepository;
import com.greenWarrior.utils.TestUtils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:/application-junit.properties")
public class TreeSearchControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private TreeRepository treeRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	private RequestSpecification requestSpecification;

	@PostConstruct
	public void initRequestSpecification() {
		RequestSpecBuilder requestSpecBuilder = new RequestSpecBuilder();
		requestSpecification = requestSpecBuilder.setBaseUri("http://localhost:" + port + "/api/tree-search")
				.setContentType(ContentType.JSON).setAccept(ContentType.JSON)
				/*
				 * .addHeader("Authorization", "Basics" +
				 * Base64.getEncoder().encodeToString(("admin:12345").getBytes()))
				 */
				.build();

	}

	@BeforeEach
	public void setUp() {
		treeRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	public void testGetTreeByDate_shouldReturnTree_whenDateMatches() {
		
		//Given

		assertEquals(0, treeRepository.count());
		assertEquals(0, userRepository.count());
		
		String user1UserName = "sita_ram_01";
		String user1password = "12345";

		User user1 = TestUtils.createUser();
		user1.setRole(Role.USER);
		user1.setUserName(user1UserName);
		user1.setPassword(passwordEncoder.encode(user1password));

		Tree tree1 = TestUtils.createTree("Mango", "Mangifera Indica", GrowthStage.YOUNG, 9.098, 9.092,
				"Near School gate", LocalDate.now(), user1.getUserName(), user1);

		Tree tree2 = TestUtils.createTree("Pineapple", "Pineapple Indica", GrowthStage.SEEDLING, 9.798, 9.002,
				"Near Temple gate", LocalDate.now(), user1.getUserName(), user1);

		
		List<Tree> listOfTrees = new ArrayList<>();
		listOfTrees.add(tree1);
		listOfTrees.add(tree2);
		user1.setTrees(listOfTrees);
		
		userRepository.save(user1);
		
		
	
		assertEquals(1, userRepository.count());
		assertEquals(2, treeRepository.count());
		
		//When
		ValidatableResponse validatableResponse = RestAssured.given()
				.spec(requestSpecification).auth().preemptive()
				.basic(user1UserName, user1password).queryParam("name", "Mango")
				.when().get("/by-local-name").then().statusCode(200);
		
		//Then
		
		List<TreeResponseDTO> response = validatableResponse.extract().as(new TypeRef<List<TreeResponseDTO>>() {});
		assertNotNull(response);
		assertEquals("Mango", response.get(0).getLocalName());
		
		

	}

}
