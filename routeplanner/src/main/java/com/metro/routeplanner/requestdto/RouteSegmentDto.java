package com.metro.routeplanner.requestdto;

import java.util.List;

import com.metro.routeplanner.responsedto.StationResponseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteSegmentDto {

	private String lineName;
	private List<StationResponseDto> stations;
	private String interchangeStation;

}
