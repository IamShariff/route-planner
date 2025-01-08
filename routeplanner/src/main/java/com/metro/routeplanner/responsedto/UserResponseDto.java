package com.metro.routeplanner.responsedto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class UserResponseDto {
	
	private String id;
	private String email;
	private BigDecimal walletBalance;
	private LocalDateTime lastLogin;
	private LocalDateTime updatedAt;
}
