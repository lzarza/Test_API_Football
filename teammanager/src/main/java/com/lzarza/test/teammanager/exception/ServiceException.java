package com.lzarza.test.teammanager.exception;

public class ServiceException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String CANNOT_CREATE_PLAYER = "CANNOT_CREATE_PLAYER";
	public static final String PLAYER_NOT_FOUND = "PLAYER_NOT_FOUND";
	public static final String MISSING_PLAYER_ID = "MISSING_PLAYER_ID";
	public static final String CANNOT_CREATE_TEAM = "CANNOT_CREATE_TEAM";
	public static final String TEAM_NOT_FOUND = "TEAM_NOT_FOUND";
	public static final String MISSING_TEAM_ID = "MISSING_TEAM_ID";
	
	private final String errorCode;
	
	public ServiceException(String errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return errorCode;
	}

}
