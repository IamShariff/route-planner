package com.metro.routeplanner.util;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHandler {

	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status, Object responseObj) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("status", status);
		map.put("data", responseObj);
		return new ResponseEntity<>(map, status);
	}

	/**
	 * Generate a ResponseEntity with a custom message and HTTP status.
	 *
	 * @param message The custom message to include in the response.
	 * @param status  The HTTP status to set for the response.
	 * @return A ResponseEntity containing the custom message and HTTP status.
	 */
	public static ResponseEntity<Object> generateResponse(String message, HttpStatus status) {
		Map<String, Object> map = new HashMap<>();
		map.put("message", message);
		map.put("status", status);
		return new ResponseEntity<>(map, status);
	}

	public static ResponseEntity<Object> generateError(String fieldName, String message, HttpStatus status) {
		Map<String, Object> map = new HashMap<>();
		map.put("fieldName", fieldName);
		map.put("message", message);
		map.put("status", status);
		return new ResponseEntity<>(map, status);
	}

}

