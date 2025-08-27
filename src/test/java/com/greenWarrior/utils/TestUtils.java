package com.greenWarrior.utils;

import java.time.LocalDate;

import com.greenWarrior.dto.request.TreeRequestDTO;
import com.greenWarrior.dto.request.UserRequestDTO;
import com.greenWarrior.dto.response.UserResponseDto;
import com.greenWarrior.enums.ActiveStatus;
import com.greenWarrior.enums.GrowthStage;
import com.greenWarrior.enums.Role;
import com.greenWarrior.model.Address;
import com.greenWarrior.model.Tree;
import com.greenWarrior.model.User;

public class TestUtils {

	public static User createUser() {
		Address address = new Address();
		address.setStreet("1-43 chinna eklara");
		address.setCity("Madnoor");
		address.setDistrict("Kamareddy");
		address.setState("Telangana");
		address.setCountry("India");
		address.setPincode("503309");

		User user = new User();

		user.setFirstName("Sita");
		user.setLastName("Ram");
		user.setEmail("sitaram@gmail.com");
		user.setPhone("9951836251");
		user.setActive(ActiveStatus.ACTIVE);
		user.setRole(Role.USER);
		user.setAddress(address);

		return user;
	}

	public static UserRequestDTO createUserRequest(User user) {
		Address address = new Address();
		address.setStreet("1-43 chinna eklara");
		address.setCity("Madnoor");
		address.setDistrict("Kamareddy");
		address.setState("Telangana");
		address.setCountry("India");
		address.setPincode("503309");

		UserRequestDTO request = new UserRequestDTO();

		request.setFirstName("Sita");
		request.setLastName("Ram");
		request.setEmail("sitaram@gmail.com");
		request.setPhone("9951836251");
		request.setActive(ActiveStatus.ACTIVE);
		request.setAddress(address);

		return request;
	}

	public static UserResponseDto getUserResponse(User user) {
		UserResponseDto response = new UserResponseDto();
		response.setId(user.getId());
		response.setFirstName(user.getFirstName());
		response.setLastName(user.getLastName());
		response.setEmail(user.getEmail());
		response.setPhone(user.getPhone());
		response.setRole(user.getRole());
		response.setTreesPlanted(user.getTreesPlanted());
		response.setActive(user.getActive());
		response.setUserName(user.getUserName());
		response.setAddress(user.getAddress());
		response.setTrees(user.getTrees());

		return response;

	}

	public static Tree createTree(String localName, String speciesName, GrowthStage growthStage, double longitude,
			double latitude, String nearByLocation, LocalDate date, String plantedBy, User user)

	{
		Tree tree = new Tree();
		tree.setLocalName(localName);
		tree.setSpecies(speciesName);
		tree.setGrowthStage(growthStage);
		tree.setLongitude(longitude);
		tree.setLatitude(latitude);
		tree.setNearByLocation("Near School Gate");
		tree.setDateOfPlantation(LocalDate.now());
		tree.setPlantedBy(plantedBy);
		tree.setUser(user);
		return tree;
	}

	public static TreeRequestDTO createTreeRequest(Tree tree) {
		TreeRequestDTO treeRequest = new TreeRequestDTO();
		treeRequest.setLocalName("Mango");
		treeRequest.setSpecies("Mangifera Indica");
		treeRequest.setGrowthStage(GrowthStage.SEEDLING);
		treeRequest.setLongitude(9.028);
		treeRequest.setLatitude(8.082);
		treeRequest.setNearByLocation("Near School Gate");

		return treeRequest;
	}
}
