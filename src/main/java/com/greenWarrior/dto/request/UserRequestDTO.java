package com.greenWarrior.dto.request;

import java.util.List;

import com.greenWarrior.enums.ActiveStatus;
import com.greenWarrior.model.Address;
import com.greenWarrior.model.Tree;



public class UserRequestDTO {

	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String userName;
	private String password;
	private ActiveStatus active;
	private Address address;
	private List<Tree> trees;

	public UserRequestDTO() {

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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public ActiveStatus getActive() {
		return active;
	}

	public void setActive(ActiveStatus active) {
		this.active = active;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

}
