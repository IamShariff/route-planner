package com.metro.routeplanner.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.metro.routeplanner.responsedto.ErrorResponseDto;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	  /**
     * Handles MethodArgumentNotValidException.
     *
     * @param message The MethodArgumentNotValidException instance.
     * @return ResponseEntity with a map of field errors and HTTP status 400 (Bad Request).
     * @implNote This method is triggered when field validations fail.
     */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgsNotValidExceptionHandler(
			MethodArgumentNotValidException messege) {
		Map<String, String> response = new HashMap<>();
		
		messege.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String defaultMessage = error.getDefaultMessage();
			response.put(fieldName, defaultMessage);
		});

		return new ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}
	/**
     * Handles GenericException.
     *
     * @param message The GenericException instance.
     * @return ResponseEntity with the error message, field name, and the specified HTTP status.
     */
	@ExceptionHandler(GenericException.class)
	public ResponseEntity<ErrorResponseDto> genericExceptionHandler(GenericException genericException) {
		final String errorMessage = genericException.getMessage();
		final String fieldName = genericException.getFieldName();
		final HttpStatus errorCode = genericException.getHttpStatusCode();
		final ErrorResponseDto errorResponse = new ErrorResponseDto(fieldName,errorMessage);
		return new ResponseEntity<ErrorResponseDto>(errorResponse,errorCode );
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> response = new HashMap<>();
        
        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            response.put(fieldName, message);
        });

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}

