package com.greenWarrior.mapper;

import com.greenWarrior.dto.response.AddressResponseDto;
import com.greenWarrior.model.Address;

public class AddressMapper {

	public static AddressResponseDto getAddressResponse(Address address) {
		AddressResponseDto response = new AddressResponseDto();
		response.setId(address.getId());
		response.setStreet(address.getStreet());
		response.setCity(address.getCity());
		response.setDistrict(address.getDistrict());
		response.setState(address.getState());
		response.setCountry(address.getCountry());
		response.setPincode(address.getPincode());

		return response;
	}

	public static Address toEntity(AddressResponseDto request) {
		Address address = new Address();

		address.setStreet(request.getStreet());
		address.setCity(request.getCity());
		address.setDistrict(request.getDistrict());
		address.setState(request.getState());
		address.setCountry(request.getCountry());
		address.setPincode(request.getPincode());

		return address;
	}
}
