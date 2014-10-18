package com.ssi.model;

public class AuthorizationException extends Exception {
	private static final long serialVersionUID = 8648462927177619032L;
	private String message;
	
	public AuthorizationException(String message) {
		this.message = message;
	}

	public String getMessage(){
		return this.message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
