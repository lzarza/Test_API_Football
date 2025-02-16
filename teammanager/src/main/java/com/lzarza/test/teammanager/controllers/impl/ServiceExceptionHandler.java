package com.lzarza.test.teammanager.controllers.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.lzarza.test.teammanager.exception.ServiceException;

/**
 * This class is design to handle different cases of service exception as http responses.
 */
public class ServiceExceptionHandler {

	/**
	 * Handle the result of a service exception
	 * Create errors are conflict
	 * Missing parameters are bad requests
	 * Entities not found are unprocessable rather than not found to avoid to mix with the url path not found error
	 * Other cases are treated as internal server error
	 * Update this method when you add new Service Exception error codes
	 * @param exception
	 * @return
	 */
	public static ResponseEntity<Object> exceptionToResponse(ServiceException exception) {
		switch (exception.getErrorCode()) {
		case ServiceException.CANNOT_CREATE_PLAYER:
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot create player");
		case ServiceException.CANNOT_CREATE_TEAM:
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Cannot create team");
		case ServiceException.MISSING_PLAYER_ID:
			return ResponseEntity.badRequest().body("missing player id");
		case ServiceException.MISSING_TEAM_ID:
			return ResponseEntity.badRequest().body("missing team id");
		case ServiceException.PLAYER_NOT_FOUND:
			return ResponseEntity.unprocessableEntity().body("Player not found");
		case ServiceException.TEAM_NOT_FOUND:
			return ResponseEntity.unprocessableEntity().body("Team not found");				
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unknown error");
	}
	
}
