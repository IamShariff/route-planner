package com.metro.routeplanner.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.metro.routeplanner.requestdto.RouteRequestDto;
import com.metro.routeplanner.responsedto.RouteResponseDto;
import com.metro.routeplanner.service.RouteService;
import com.metro.routeplanner.util.LogRequest;
import com.metro.routeplanner.util.LogResponse;
import com.metro.routeplanner.util.LogTime;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/route")
@Tag(name = "Route Controller", description = "APIs for finding metro routes between stations")
public class RouteController {

	private final RouteService routeService;

	/**
	 * API to get the route between two stations, including interchanges if
	 * necessary. This API returns a list of stations that form the route between
	 * the source and destination stations.
	 * 
	 * @param sourceStation      The name of the source station from which the
	 *                           journey starts.
	 * @param destinationStation The name of the destination station where the
	 *                           journey ends.
	 * @return ResponseEntity<List<RouteResponseDto>> A response entity containing
	 *         the route information.
	 */
	@Operation(summary = "Find routes between two stations", description = "Returns multiple route options based on preferences")
	@PostMapping("/findRoutes")
	@LogRequest
	@LogResponse
	@LogTime
	public ResponseEntity<List<RouteResponseDto>> getRouteBetweenStations(
			@Valid @RequestBody RouteRequestDto routeRequestDto) {
		return ResponseEntity.ok(routeService.getRouteBetweenStations(routeRequestDto));
	}
}