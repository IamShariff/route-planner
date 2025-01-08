package com.metro.routeplanner.service.impl;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.metro.routeplanner.model.PaymentMethod;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FareCalculatorService {

	private final RouteFinderJGraphT routeFinder;

	private static final int[] STOP_RANGES = { 2, 5, 12, 21, 32 };
	private static final int[] FARES = { 10, 20, 30, 40, 50, 60 }; // Last fare for stops above 32
	private static final LocalTime MORNING_PEAK_START = LocalTime.of(8, 0);
    private static final LocalTime MORNING_PEAK_END = LocalTime.of(10, 0);
    private static final LocalTime EVENING_PEAK_START = LocalTime.of(17, 0);
    private static final LocalTime EVENING_PEAK_END = LocalTime.of(20, 0);

	public double calculateFare(String sourceStationName, String destinationStationName, PaymentMethod paymentMethod) {
		int stops = calculateStopsBetweenStations(sourceStationName, destinationStationName);
		int baseFare = getBaseFareByStops(stops);
		boolean isOffPeak = !isPeakHour();
		boolean isSmartCard = paymentMethod.equals(PaymentMethod.SMART_CARD);
		boolean isHoliday = false;
		
		// Apply holiday discount if applicable, will implement it later
		if (isHoliday) {
			baseFare = Math.max(0, baseFare - 10); // Flat ₹10 holiday discount
		}

		// Apply discounts for Smart Card and off-peak hours if applicable
		double totalFare = baseFare;
		if (isSmartCard) {
			totalFare -= baseFare * 0.10; // 10% discount for Smart Card
		}
		if (isOffPeak) {
			totalFare -= baseFare * 0.10; // Additional 10% off-peak discount
		}

		return Math.round(totalFare * 100.0) / 100.0;
	}

	private int getBaseFareByStops(int stops) {
		for (int i = 0; i < STOP_RANGES.length; i++) {
			if (stops <= STOP_RANGES[i]) {
				return FARES[i];
			}
		}
		return FARES[FARES.length - 1]; // Max fare for stops above highest range
	}

	private int calculateStopsBetweenStations(String sourceStationName, String destinationStationName) {
		List<List<String>> paths = routeFinder.findKShortestPaths(sourceStationName, destinationStationName, 1);

		if (paths.isEmpty()) {
			throw new RuntimeException(
					"No route found between " + sourceStationName + " and " + destinationStationName);
		}

		// Shortest path will be the first in the list
		List<String> shortestPath = paths.get(0);

		// Stops count is path length - 1 (since stops are the spaces between stations)
		return shortestPath.size() - 1;
	}

	private boolean isPeakHour() {
		LocalTime now = LocalTime.now();
		return (now.isAfter(MORNING_PEAK_START) && now.isBefore(MORNING_PEAK_END))
				|| (now.isAfter(EVENING_PEAK_START) && now.isBefore(EVENING_PEAK_END));
	}

}
