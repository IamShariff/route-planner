package com.metro.routeplanner.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;

	private String sourceStation;
	
	private String destinationStation;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Payment method must be specified")
	@Column(nullable = false)
	private PaymentMethod paymentMethod;

	@NotNull(message = "Creation time must be provided")
	private LocalDateTime createdAt;

	@NotNull(message = "Expiry time must be provided")
	@Future(message = "Expiry time must be in the future")
	private LocalDateTime expiryTime;

	private String userId;

	private boolean isExpired;

	private double fare;

	private boolean usedAtSource;

	private boolean usedAtDestination;
}