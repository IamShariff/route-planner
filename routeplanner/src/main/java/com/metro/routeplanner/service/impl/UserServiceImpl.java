package com.metro.routeplanner.service.impl;

import com.metro.routeplanner.exception.AlreadyExistException;
import com.metro.routeplanner.exception.NotFoundException;
import com.metro.routeplanner.model.Role;
import com.metro.routeplanner.model.User;
import com.metro.routeplanner.requestdto.AddUserRequestDto;
import com.metro.routeplanner.requestdto.UpdateWalletRequestDto;
import com.metro.routeplanner.responsedto.UserResponseDto;
import com.metro.routeplanner.service.UserService;
import com.metro.routeplanner.dao.UserDao;
import com.metro.routeplanner.util.Constant;
import com.metro.routeplanner.util.UserExtractor;

import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, GraphQLMutationResolver {

	private final UserDao userDao;
	private final ModelMapper modelMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public UserResponseDto addUser(AddUserRequestDto addUserRequestDto) {
		// Check if a user with the provided email already exists
		if (userDao.existsByEmail(addUserRequestDto.email())) {
			throw new AlreadyExistException(Constant.EMAIL, Constant.USER_ALREADY_EXIST);
		}

		// Create and save the user
		User user = new User();
		user.setEmail(addUserRequestDto.email());
		user.setPassword(passwordEncoder.encode(addUserRequestDto.password()));
		user.setRole(Role.USER); // Assuming a default role
		user.setWalletBalance(BigDecimal.ZERO);
		user.setLastLogin(LocalDateTime.now());
		userDao.save(user);

		// Map to UserResponseDto and return
		return modelMapper.map(user, UserResponseDto.class);
	}

	@Override
	public UserResponseDto editUser(AddUserRequestDto editUserRequestDto) {
		User existingUser = userDao.findById(UserExtractor.getUserId())
				.orElseThrow(() -> new NotFoundException(Constant.USER, Constant.USER_NOT_FOUND));

		if (userDao.existsByEmail(editUserRequestDto.email())) {
			throw new AlreadyExistException(Constant.EMAIL, Constant.USER_ALREADY_EXIST);
		}

		// Update user details and save
		existingUser.setEmail(editUserRequestDto.email());
		existingUser.setPassword(passwordEncoder.encode(editUserRequestDto.password()));
		userDao.save(existingUser);

		// Map to UserResponseDto and return
		return modelMapper.map(existingUser, UserResponseDto.class);
	}

	@Override
	public UserResponseDto updateWalletBalance(UpdateWalletRequestDto walletRequestDto) {
		User existingUser = userDao.findById(UserExtractor.getUserId())
				.orElseThrow(() -> new NotFoundException(Constant.USER, Constant.USER_NOT_FOUND));

		existingUser.setWalletBalance(existingUser.getWalletBalance().add(walletRequestDto.amount()));
		userDao.save(existingUser);

		// Map to UserResponseDto and return
		return modelMapper.map(existingUser, UserResponseDto.class);
	}
}
