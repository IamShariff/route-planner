package com.metro.routeplanner.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.metro.routeplanner.model.Line;
import com.metro.routeplanner.model.Station;
import com.metro.routeplanner.requestdto.AddLineRequestDto;
import com.metro.routeplanner.requestdto.AddStationRequestDto;
import com.metro.routeplanner.service.MetroService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/metro")
@Tag(name = "Metro Controller API", description = "API for managing metro stations and lines")
public class MetroController {

	private final MetroService metroService;

	/**
	 * API to add a new station.
	 * 
	 * @param addStationRequestDto - Details of the station to add.
	 * @return Response containing a success message and the added station details.
	 */
	@PostMapping("/station")
	@Operation(summary = "Adds a new metro station to the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Station added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddStationRequestDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Station> addStation(
			@Parameter(description = "Details of the station to add", required = true) @Valid @RequestBody AddStationRequestDto addStationRequestDto) {
		return new ResponseEntity<>(metroService.addStation(addStationRequestDto), HttpStatus.CREATED);
	}

	/**
	 * API to edit an existing station.
	 * 
	 * @param stationId            - ID of the station to edit.
	 * @param addStationRequestDto - Updated station details.
	 * @return Response containing a success message and the updated station
	 *         details.
	 */
	@PutMapping("/station/{stationId}")
	@Operation(summary = "Modifies details of an existing metro station")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Station updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddStationRequestDto.class))),
			@ApiResponse(responseCode = "404", description = "Station not found", content = @Content) })
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Station> editStation(
			@Parameter(description = "ID of the station to edit", required = true) @Valid @PathVariable String stationId,
			@Parameter(description = "Updated station details", required = true) @RequestBody AddStationRequestDto addStationRequestDto) {
		return new ResponseEntity<>(metroService.editStation(stationId, addStationRequestDto),
				HttpStatus.CREATED);
	}

	/**
	 * API to fetch a station by its ID.
	 * 
	 * @param stationId - ID of the station to retrieve.
	 * @return Response containing the station details.
	 */
	@GetMapping("/station/{stationId}")
	@Operation(summary = "Retrieve details of a metro station by its ID")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Station fetched successfully", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Station not found", content = @Content) })
	public ResponseEntity<Station> getStationById(
			@Parameter(description = "ID of the station to fetch", required = true) @Valid @PathVariable String stationId) {
		return ResponseEntity.ok(metroService.getStationById(stationId));
	}

	/**
	 * API to fetch all stations for a specific metro line.
	 * 
	 * @param lineName - Name of the metro line.
	 * @return Response containing a list of stations for the specified line.
	 */
	@GetMapping("/line/{lineName}")
	@Operation(summary = "Retrieve all stations for a specific metro line")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Stations fetched successfully", content = @Content(mediaType = "application/json")),
			@ApiResponse(responseCode = "404", description = "Line not found", content = @Content) })
	public ResponseEntity<List<Station>> getStationsByLine(
			@Parameter(description = "Name of the line", required = true) @Valid @PathVariable String lineName) {
		return ResponseEntity.ok(metroService.getStationsByLine(lineName));
	}

	/**
	 * API to add a new metro line.
	 * 
	 * @param addLineRequestDto - Details of the line to add.
	 * @return Response containing a success message and the added line details.
	 */
	@PostMapping("/line")
	@Operation(summary = "Adds a new metro line to the database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Line added successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddLineRequestDto.class))),
			@ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<Line> addLine(
			@Parameter(description = "Details of the line to add", required = true) @Valid @RequestBody AddLineRequestDto addLineRequestDto) {
		return new ResponseEntity<>(metroService.addLine(addLineRequestDto), HttpStatus.CREATED);
	}

	/**
	 * API to add multiple stations at once.
	 * 
	 * @param addStationRequestDtoList - List of station details to add.
	 * @return Response containing a success message and the list of added stations.
	 */
	@PostMapping("/add-stations")
	@Operation(summary = "Adds multiple metro stations in a single request")
	@ApiResponses(value = {
	        @ApiResponse(responseCode = "201", description = "Stations added successfully", content = @Content(mediaType = "application/json")),
	        @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content) })
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<List<Station>> addStations(
	        @Parameter(description = "List of stations to add", required = true) @RequestBody List<AddStationRequestDto> addStationRequestDtoList) {
	    
	    return new ResponseEntity<>(metroService.addStations(addStationRequestDtoList), HttpStatus.CREATED);
	}
}