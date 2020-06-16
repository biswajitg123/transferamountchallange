package com.db.awmd.challenge.exception;

public class AmountGreaterThanAvailableBalanceException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public AmountGreaterThanAvailableBalanceException(String message) {
	    super(message);
	  }
}
