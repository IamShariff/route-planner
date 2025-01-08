package com.metro.routeplanner.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import com.metro.routeplanner.controller.MetroController;
import com.metro.routeplanner.exception.NotFoundException;
import com.metro.routeplanner.model.LineIndex;
import com.metro.routeplanner.model.PaymentMethod;
import com.metro.routeplanner.model.RoutePreference;
import com.metro.routeplanner.model.Station;
import com.metro.routeplanner.requestdto.RouteRequestDto;
import com.metro.routeplanner.requestdto.RouteSegmentDto;
import com.metro.routeplanner.responsedto.RouteResponseDto;
import com.metro.routeplanner.responsedto.StationResponseDto;
import com.metro.routeplanner.service.RouteService;
import com.metro.routeplanner.util.RouteUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RouteServiceImpl implements RouteService {

	private final RouteUtil routeUtil;
	private final FareCalculatorService fareCalculatorService;
	private final RouteFinderJGraphT routeFinderJGraphT;

	@Override

	public List<RouteResponseDto> getRouteBetweenStations(RouteRequestDto routeRequestDto) {

		List<List<String>> allPaths = routeFinderJGraphT.findKShortestPaths(routeRequestDto.sourceStation(),
				routeRequestDto.destinationStation(), routeRequestDto.numRoutes());

		// Sort paths based on preference
		List<List<String>> sortedPaths;
		if (RoutePreference.MINIMUM_INTERCHANGES.equals(routeRequestDto.preference())) {
			sortedPaths = allPaths.stream().sorted(Comparator.comparingInt(this::calculateInterchanges)).toList();
		} else {
			// Default is sorting by minimum stations
			sortedPaths = allPaths.stream().sorted(Comparator.comparingInt(List::size)).toList();
		}

		// Limit to the requested number of routes
		List<List<String>> selectedPaths = sortedPaths.stream().limit(routeRequestDto.numRoutes()).toList();

		// Convert paths to RouteResponseDto objects
		return selectedPaths.stream().map(this::buildRouteResponse).toList();
	}

	private int calculateInterchanges(List<String> path) {
		int interchanges = 0;
		String currentLine = null;

		for (int i = 0; i < path.size() - 1; i++) {
			String line = getLineBetweenStations(path.get(i), path.get(i + 1));
			if (line != null && !line.equals(currentLine)) {
				interchanges++;
				currentLine = line;
			}
		}

		return interchanges;
	}

	private RouteResponseDto buildRouteResponse(List<String> path) {
		RouteResponseDto responseDto = new RouteResponseDto();
		List<RouteSegmentDto> segments = new ArrayList<>();

		if (path.isEmpty()) {
			return responseDto;
		}

		String currentLine = null;
		RouteSegmentDto currentSegment = null;
		int interchangeCount = 0; // Counter for total interchanges

		// Iterate through the path to identify segments and interchanges
		for (int i = 0; i < path.size() - 1; i++) {
			String currentStation = path.get(i);
			String nextStation = path.get(i + 1);
			String lineName = getLineBetweenStations(currentStation, nextStation);

			if (lineName == null) {
				throw new NotFoundException("line",
						"No connecting line found between " + currentStation + " and " + nextStation);
			}

			// Check if line changes, indicating an interchange
			if (!lineName.equals(currentLine)) {
				if (currentSegment != null) {
					// Set the interchange station before changing lines
					currentSegment.setInterchangeStation(currentStation);
					segments.add(currentSegment);
					interchangeCount++; // Increment the interchange count
				}
				currentLine = lineName;
				currentSegment = new RouteSegmentDto();
				currentSegment.setLineName(currentLine);
				currentSegment.setStations(new ArrayList<>());
			}

			// Add current station to the segment
			StationResponseDto stationResponse = createStationResponseDto(currentStation);
			currentSegment.getStations().add(stationResponse);
		}

		// Add the last station
		String lastStation = path.get(path.size() - 1);
		StationResponseDto lastStationResponse = createStationResponseDto(lastStation);
		if (currentSegment == null) {
			currentSegment = new RouteSegmentDto();
			currentSegment.setStations(new ArrayList<>());
		}
		currentSegment.getStations().add(lastStationResponse);
		segments.add(currentSegment);

		responseDto.setSegments(segments);

		double fare = fareCalculatorService.calculateFare(path.get(0), path.get(path.size() - 1), PaymentMethod.CASH);

		// Calculate total stops, time, fare, and set interchange count
		int totalStops = path.size() - 1;
		responseDto.setTotalStops(totalStops);
		responseDto.setTotalTimeInMinutes(totalStops * 2); // Adjust as needed
		responseDto.setFare(fare); // Implement fare calculation
		responseDto.setTotalInterchanges(interchangeCount); // Set total interchanges

		return responseDto;
	}

	private String getLineBetweenStations(String fromStation, String toStation) {
		Station from = routeUtil.findStationByName(fromStation);
		Station to = routeUtil.findStationByName(toStation);

		Set<String> linesFrom = from.getLineIndex().stream().map(LineIndex::getLineName).collect(Collectors.toSet());

		Set<String> linesTo = to.getLineIndex().stream().map(LineIndex::getLineName).collect(Collectors.toSet());

		linesFrom.retainAll(linesTo); // Intersection of lines

		if (!linesFrom.isEmpty()) {
			return linesFrom.iterator().next();
		}

		return null; // No common line found
	}

	private StationResponseDto createStationResponseDto(String stationName) {
		StationResponseDto stationResponse = new StationResponseDto();
		stationResponse.setName(stationName);

		Station station = routeUtil.findStationByName(stationName);
		Link link = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MetroController.class).getStationById(station.getId()))
				.withSelfRel();
		stationResponse.add(link);

		return stationResponse;
	}
}
