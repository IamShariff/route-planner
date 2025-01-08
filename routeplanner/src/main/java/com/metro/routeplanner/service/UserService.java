package com.metro.routeplanner.service;

import com.metro.routeplanner.requestdto.AddUserRequestDto;
import com.metro.routeplanner.requestdto.UpdateWalletRequestDto;
import com.metro.routeplanner.responsedto.UserResponseDto;

public interface UserService {
	
	UserResponseDto addUser(AddUserRequestDto addUserRequestDto);

	UserResponseDto editUser(AddUserRequestDto editUserRequestDto);

	UserResponseDto updateWalletBalance(UpdateWalletRequestDto walletRequestDto);
}
