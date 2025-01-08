package com.metro.routeplanner.controller;

import java.io.IOException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.metro.routeplanner.requestdto.BuyTicketRequestDto;
import com.metro.routeplanner.service.TicketService;
import com.metro.routeplanner.util.Constant;
import com.metro.routeplanner.util.LogRequest;
import com.metro.routeplanner.util.LogResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('USER')")
@Tag(name = "Ticket Controller API", description = "APIs for handling metro tickets")
public class TicketController {

	private final TicketService ticketService;

	/**
	 * API to purchase a metro ticket. This API generates a QR code containing the
	 * ticket details.
	 * 
	 * @param dto The request DTO containing the details of the ticket purchase
	 *            (source, destination, etc.).
	 * @return ResponseEntity<byte[]> with the generated QR code image if the
	 *         purchase is successful, or an internal server error if the purchase
	 *         fails.
	 */
	@LogRequest
	@LogResponse
	@Operation(summary = "Purchase a ticket", description = "Generates a QR code for the purchased metro ticket")
	@PostMapping("/buy-ticket")
	public ResponseEntity<byte[]> buyTicket(@Valid @RequestBody BuyTicketRequestDto dto) {
		try {
			byte[] qrCode = ticketService.buyTicket(dto);

			// Set HTTP headers to define the QR code as a PNG image
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);
			headers.setContentLength(qrCode.length);

			// Return the QR code as a response with an OK status
			return new ResponseEntity<>(qrCode, headers, HttpStatus.OK);

		} catch (IOException e) {
			// Return an internal server error if QR code generation or purchase fails
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * API to use a ticket at the source station. This API validates the ticket
	 * using the uploaded QR code and the provided source station.
	 * 
	 * @param file          The QR code image as a MultipartFile.
	 * @param sourceStation The name of the source station where the ticket is being
	 *                      used.
	 * @return ResponseEntity<String> with a success or failure message.
	 */
	@Operation(summary = "Use ticket at source station", description = "Validates the ticket at the source station using the uploaded QR code")
	@PostMapping("/upload-qr-source")
	public ResponseEntity<String> useTicketAtSource(@Valid @RequestParam("qrCode") MultipartFile file,
			@RequestParam String sourceStation) {

		// Process the ticket for source station usage
		return ticketService.processTicket(file, sourceStation, true)
				? ResponseEntity.ok(Constant.TICKET_USED_SOURCE_SUCCESS) // Success message
				: ResponseEntity.badRequest().body(Constant.TICKET_USED_SOURCE_FAILURE); // Failure message
	}

	/**
	 * API to use a ticket at the destination station. This API validates the ticket
	 * using the uploaded QR code and the provided destination station.
	 * 
	 * @param file               The QR code image as a MultipartFile.
	 * @param destinationStation The name of the destination station where the
	 *                           ticket is being used.
	 * @return ResponseEntity<String> with a success or failure message.
	 */
	@Operation(summary = "Use ticket at destination station", description = "Validates the ticket at the destination station using the uploaded QR code")
	@PostMapping("/upload-qr-destination")
	public ResponseEntity<String> useTicketAtDestination(@Valid @RequestParam("qrCode") MultipartFile file,
			@Valid @RequestParam String destinationStation) {
		

		// Process the ticket for destination station usage
		return ticketService.processTicket(file, destinationStation, false)
				? ResponseEntity.ok(Constant.TICKET_USED_DEST_SUCCESS) // Success message
				: ResponseEntity.badRequest().body(Constant.TICKET_USED_DEST_FAILURE); // Failure message
	}

}