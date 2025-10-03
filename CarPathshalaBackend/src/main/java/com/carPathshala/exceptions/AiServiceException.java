package com.carPathshala.exceptions;

public class AiServiceException extends RuntimeException{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AiServiceException(String message) {
	        super(message);
	    }
	    public AiServiceException(String message, Throwable cause) {
	        super(message, cause);
	    }
}
