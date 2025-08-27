package com.greenWarrior.dto.response;

import java.util.List;

import com.greenWarrior.enums.ActiveStatus;
import com.greenWarrior.enums.Role;
import com.greenWarrior.model.Address;
import com.greenWarrior.model.Tree;

public class UserResponseDto {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private Role role;
	private int treesPlanted;
	private ActiveStatus active;
	private String userName;
	private Address address;
	private List<Tree> trees;

	public UserResponseDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public int getTreesPlanted() {
		return treesPlanted;
	}

	public void setTreesPlanted(int treesPlanted) {
		this.treesPlanted = treesPlanted;
	}

	public ActiveStatus getActive() {
		return active;
	}

	public void setActive(ActiveStatus active) {
		this.active = active;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Tree> getTrees() {
		return trees;
	}

	public void setTrees(List<Tree> trees) {
		this.trees = trees;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserResponseDto [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phone=" + phone + ", role=" + role + ", treesPlanted=" + treesPlanted + ", active=" + active
				+ ", userName=" + userName + ", address=" + address + ", trees=" + trees + "]";
	}

}
