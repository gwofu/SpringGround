package com.devry.mobile.utils;

/**
 * Thrown when the Connection to BANNER Database is not established.
 *
 */
public class BannerConnectionException extends Exception {

	/**
	 * Exceptions for the Banner Connection
	 */
	private static final long serialVersionUID = 1L;
	public BannerConnectionException() {
		super("Connection to Banner could not be establised.");
	}
	public BannerConnectionException(String message) {
		super(message);	
	}
	public BannerConnectionException(Throwable cause) {
		super(cause);	
	}
	public BannerConnectionException(String message, Throwable cause) {
		super(message, cause);	
	}
}
