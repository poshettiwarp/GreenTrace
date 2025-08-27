package com.greenWarrior.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse hadnleResourceNotFoundException(ResourceNotFoundException e) {
		ErrorResponse response = new ErrorResponse();
		response.setCode(e.getCode());
		response.setMessage(e.getMessage());

		return response;
	}

	@ExceptionHandler(DuplicateResourceException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleDuplicateResourceException(DuplicateResourceException e) {
		ErrorResponse response = new ErrorResponse();
		response.setCode(e.getCode());
		response.setMessage(e.getMessage());

		return response;
	}

	@ExceptionHandler(InvalidRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorResponse handleInvalidRequestException(InvalidRequestException e) {
		ErrorResponse response = new ErrorResponse();
		response.setCode(e.getCode());
		response.setMessage(e.getMessage());

		return response;
	}

	@ExceptionHandler(DuplicateUserException.class)
	@ResponseStatus(HttpStatus.CONFLICT)
	public ErrorResponse handleDuplicateUserException(DuplicateUserException e) {
		ErrorResponse response = new ErrorResponse();
		response.setCode(e.getCode());
		response.setMessage(e.getMessage());

		return response;
	}

	@ExceptionHandler(UserNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
		ErrorResponse response = new ErrorResponse();
		response.setCode(e.getCode());
		response.setMessage(e.getMessage());
		return response;
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
		ErrorResponse response=new ErrorResponse();
		response.setCode(HttpStatus.FORBIDDEN.toString());
		response.setMessage(e.getMessage());
		return response;
	}

	/*
	 * @ExceptionHandler(Throwable.class)
	 * 
	 * @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) public ErrorResponse
	 * handleThrowable(Throwable e) { ErrorResponse response = new ErrorResponse();
	 * response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.toString());
	 * response.setMessage("Something went wrong");
	 * 
	 * return response; }
	 */

}
