package com.metro.routeplanner.responsedto;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationResponseDto extends RepresentationModel<StationResponseDto> {

	private String name;

}
