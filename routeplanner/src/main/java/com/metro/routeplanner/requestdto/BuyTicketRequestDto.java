package com.metro.routeplanner.requestdto;

import com.metro.routeplanner.model.PaymentMethod;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

public record BuyTicketRequestDto(@NotBlank String sourceStation, @NotBlank String destinationStation,
		@Valid PaymentMethod paymentMethod) {
}
