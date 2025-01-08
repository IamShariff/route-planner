package com.metro.routeplanner.responsedto;

import java.util.List;

import com.metro.routeplanner.requestdto.RouteSegmentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RouteResponseDto {

	private List<RouteSegmentDto> segments;
	private int totalStops;
	private int totalTimeInMinutes;
	private double fare;
	private int totalInterchanges;
}
