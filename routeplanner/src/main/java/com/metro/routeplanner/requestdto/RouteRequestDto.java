package com.metro.routeplanner.requestdto;

import com.metro.routeplanner.model.RoutePreference;

public record RouteRequestDto(String sourceStation, String destinationStation, int numRoutes,
		RoutePreference preference) {

	public RouteRequestDto(String sourceStation, String destinationStation, Integer numRoutes,
			RoutePreference preference) {
		this(sourceStation, destinationStation, numRoutes != null ? numRoutes : 1,
				preference != null ? preference : RoutePreference.MINIMUM_STATIONS);
	}
}
