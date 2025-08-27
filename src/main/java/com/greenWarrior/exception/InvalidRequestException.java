package com.greenWarrior.exception;

public class InvalidRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private String code;

	public InvalidRequestException(String code, String message) {
		super(message);
		this.code=code;
	}

	public String getCode() {
		return code;
	}
}
