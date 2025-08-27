package com.greenWarrior.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCodes {

	RESOURCE_NOT_FOUND("101", "Resource Not Available"), DUPLICATE_RESOURCE("102", "Duplicate Resource"),
	UNAUTHORIZED_ACCESS("103", "No Access"), INVALID_REQUEST("104", "Invalid Request"), DUPLICATE_USER("105","User Already Exists"),
	USER_NOT_FOUND(HttpStatus.NOT_FOUND.toString(),"User Not Found"),ACCESS_DENIED_EXCEPTION(HttpStatus.FORBIDDEN.toString(),"Access Denied.");

	private final String code;
	private final String message;

	ErrorCodes(String code, String message) {
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

}
