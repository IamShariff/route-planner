package com.metro.routeplanner.requestdto;

import jakarta.validation.constraints.NotBlank;

public record AddLineRequestDto(@NotBlank String name, @NotBlank String source, @NotBlank String destination) {
}
