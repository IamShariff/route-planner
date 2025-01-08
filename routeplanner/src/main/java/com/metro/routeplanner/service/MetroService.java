package com.metro.routeplanner.service;

import com.metro.routeplanner.model.Line;
import com.metro.routeplanner.model.Station;
import com.metro.routeplanner.requestdto.AddLineRequestDto;
import com.metro.routeplanner.requestdto.AddStationRequestDto;

import java.util.List;

public interface MetroService {
	
	Line addLine(AddLineRequestDto addLineRequestDto);

	Station addStation(AddStationRequestDto addStationRequestDto);

	Station editStation(String stationId, AddStationRequestDto addStationRequestDto);

	Station getStationById(String stationId);

	List<Station> getStationsByLine(String lineName);

	List<Station> addStations(List<AddStationRequestDto> addStationRequestDtoList);
}
