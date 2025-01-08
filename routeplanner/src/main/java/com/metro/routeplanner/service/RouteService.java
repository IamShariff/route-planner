package com.metro.routeplanner.service;

import java.util.List;

import com.metro.routeplanner.requestdto.RouteRequestDto;
import com.metro.routeplanner.responsedto.RouteResponseDto;

public interface RouteService {

	List<RouteResponseDto> getRouteBetweenStations(RouteRequestDto routeRequestDto);

}
