package com.db.awmd.challenge.exception.handler;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.db.awmd.challenge.exception.AmountGreaterThanAvailableBalanceException;
import com.db.awmd.challenge.exception.InvalidAccountIdException;

@ControllerAdvice
public class BusinessExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidAccountIdException.class)
	public ResponseEntity<Object> invalidAccountId(InvalidAccountIdException iae) {
		return new ResponseEntity<>(iae.getMessage(), HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(AmountGreaterThanAvailableBalanceException.class)
	public ResponseEntity<Object> amountGreaterThanAvailableBalance(AmountGreaterThanAvailableBalanceException agabe) {
		return new ResponseEntity<>(agabe.getMessage(), HttpStatus.BAD_REQUEST);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return new ResponseEntity<>(ex.getBindingResult(), HttpStatus.BAD_REQUEST); 
	}

}
