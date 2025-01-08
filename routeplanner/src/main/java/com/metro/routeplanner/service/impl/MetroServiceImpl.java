package com.metro.routeplanner.service.impl;

import com.metro.routeplanner.dao.LineDao;
import com.metro.routeplanner.dao.StationDao;
import com.metro.routeplanner.exception.NotFoundException;
import com.metro.routeplanner.model.Line;
import com.metro.routeplanner.model.LineIndex;
import com.metro.routeplanner.model.Station;
import com.metro.routeplanner.requestdto.AddLineRequestDto;
import com.metro.routeplanner.requestdto.AddStationRequestDto;
import com.metro.routeplanner.service.MetroService;
import com.metro.routeplanner.util.Constant;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MetroServiceImpl implements MetroService {

	private final StationDao stationDao;
	private final LineDao lineDao;

	@Override
	public Line addLine(AddLineRequestDto addLineRequestDto) {
	    Line line = new Line();
	    line.setName(addLineRequestDto.name().toUpperCase());
	    line.setSource(addLineRequestDto.source().toUpperCase());
	    line.setDestination(addLineRequestDto.destination().toUpperCase());
	    return lineDao.save(line);
	}

	@Override
	public Station addStation(AddStationRequestDto addStationRequestDto) {
		for (LineIndex lineIndex : addStationRequestDto.lineIndex()) {
			if (!lineDao.existsByName(lineIndex.getLineName())) {
				throw new NotFoundException(Constant.LINE_NAME, Constant.LINE_NOT_FOUND);
			}
		}

		Station station = new Station();
		station.setName(addStationRequestDto.name().toUpperCase());
		station.setInterchangable(addStationRequestDto.isInterchangable());
		station.setLineIndex(addStationRequestDto.lineIndex());
		return stationDao.save(station);
	}

	@Override
	public Station editStation(String stationId, AddStationRequestDto addStationRequestDto) {
		Optional<Station> existingStationOpt = stationDao.findById(stationId);

		if (existingStationOpt.isPresent()) {
			Station station = existingStationOpt.get();
			station.setName(addStationRequestDto.name());
			station.setInterchangable(addStationRequestDto.isInterchangable());
			station.setLineIndex(addStationRequestDto.lineIndex());
			return stationDao.save(station);
		} else {
			throw new NotFoundException(Constant.STATION_ID, Constant.STATION_NOT_FOUND);
		}
	}

	@Override
	public Station getStationById(String stationId) {
		return stationDao.findById(stationId)
				.orElseThrow(() -> new NotFoundException(Constant.STATION_ID, Constant.STATION_NOT_FOUND));
	}

	@Override
	public List<Station> getStationsByLine(String lineName) {
		if (!lineDao.existsByName(lineName)) {
			throw new NotFoundException(Constant.LINE_NAME, Constant.LINE_NOT_FOUND);
		}
		return stationDao.findStationsByLineName(lineName);
	}

	@Override
	public List<Station> addStations(List<AddStationRequestDto> addStationRequestDtoList) {
		System.out.println("he");
		List<Station> savedStations = new ArrayList<>();
		for (AddStationRequestDto dto : addStationRequestDtoList) {
			for (LineIndex lineIndex : dto.lineIndex()) {
				if (!lineDao.existsByName(lineIndex.getLineName())) {
					throw new NotFoundException(Constant.LINE_NAME, Constant.LINE_NOT_FOUND + lineIndex);
				}
			}
			Station station = new Station();
			station.setName(dto.name().toUpperCase());
			station.setInterchangable(dto.isInterchangable());
			station.setLineIndex(dto.lineIndex());
			savedStations.add(stationDao.save(station));
		}
		return savedStations;
	}
}
