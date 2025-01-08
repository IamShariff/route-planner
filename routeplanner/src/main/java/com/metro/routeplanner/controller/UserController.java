package com.metro.routeplanner.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metro.routeplanner.config.JwtService;
import com.metro.routeplanner.requestdto.AddUserRequestDto;
import com.metro.routeplanner.requestdto.JwtLoginRequestDto;
import com.metro.routeplanner.requestdto.UpdateWalletRequestDto;
import com.metro.routeplanner.responsedto.UserResponseDto;
import com.metro.routeplanner.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller API", description = "API for managing users")
public class UserController {

	private final UserService userService;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	/**
	 * API to add a new user.
	 * 
	 * @param addUserRequestDto - The user details to be added.
	 * @return ResponseEntity containing UserResponseDto and HTTP status.
	 */
	@PostMapping("/add")
	@Operation(summary = "Add a new user", description = "This API allows you to add a new user to the system.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	public ResponseEntity<UserResponseDto> addUser(@Valid @RequestBody AddUserRequestDto addUserRequestDto) {
		return new ResponseEntity<>(userService.addUser(addUserRequestDto), HttpStatus.CREATED);
	}

	/**
	 * API to edit an existing user.
	 * 
	 * @param editUserRequestDto - The updated user details.
	 * @return ResponseEntity containing UserResponseDto and HTTP status.
	 */
	@PutMapping("/edit")
	@PreAuthorize("hasAuthority('USER')")
	@Operation(summary = "Edit an existing user", description = "This API allows the authenticated user to edit their details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "User details updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
			@ApiResponse(responseCode = "403", description = "Unauthorized access", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	public ResponseEntity<UserResponseDto> editUser(@Valid @RequestBody AddUserRequestDto editUserRequestDto) {
		return new ResponseEntity<>(userService.editUser(editUserRequestDto), HttpStatus.CREATED);
	}

	/**
	 * API to update the user's wallet balance.
	 * 
	 * @param walletRequestDto - The wallet update request containing the user ID
	 *                         and the amount to add or subtract.
	 * @return ResponseEntity containing UserResponseDto and HTTP status.
	 */
	@PutMapping("/wallet/update")
	@PreAuthorize("hasAuthority('USER')")
	@Operation(summary = "Update wallet balance", description = "This API allows the authenticated user to update their wallet balance.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Wallet balance updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	public ResponseEntity<UserResponseDto> updateWalletBalance(
			@Valid @RequestBody UpdateWalletRequestDto walletRequestDto) {
		return new ResponseEntity<>(userService.updateWalletBalance(walletRequestDto), HttpStatus.CREATED);
	}

	/**
	 * API to authenticate a user and generate a JWT token.
	 * 
	 * @param jwtRequest - Login request containing the user's email and password.
	 * @return ResponseEntity containing the JWT token or error message.
	 */
	@PostMapping("/authenticate")
	@Operation(summary = "Generate JWT Token", description = "This API authenticates the user and generates a JWT token for future requests.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Authentication successful, token generated", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "400", description = "Invalid credentials", content = @Content),
			@ApiResponse(responseCode = "404", description = "User not found", content = @Content) })
	public ResponseEntity<Map<String, String>> authenticateAndGetToken(
			@Valid @RequestBody JwtLoginRequestDto jwtRequest) {

		// Authenticate user credentials and handle with Optional
		return Optional
				.ofNullable(authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(jwtRequest.email(), jwtRequest.password())))
				.filter(Authentication::isAuthenticated) // Check if authentication was successful
				.map(authentication -> {
					String token = jwtService.generateToken(jwtRequest);
					Map<String, String> response = new HashMap<>();
					response.put("token", token);
					return new ResponseEntity<>(response, HttpStatus.CREATED);
				}).orElseGet(() -> ResponseEntity.badRequest().body(null)); // If authentication fails, return
																			// BAD_REQUEST
	}
}
