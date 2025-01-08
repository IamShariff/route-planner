package com.metro.routeplanner.requestdto;

import com.metro.routeplanner.model.LineIndex;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record AddStationRequestDto(@NotBlank(message = "name can't be null") String name, boolean isInterchangable,
		@NotNull List<LineIndex> lineIndex) {
}
