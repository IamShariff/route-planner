package com.metro.routeplanner.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.metro.routeplanner.dao.TicketDao;
import com.metro.routeplanner.dao.UserDao;
import com.metro.routeplanner.exception.NotFoundException;
import com.metro.routeplanner.model.Ticket;
import com.metro.routeplanner.model.User;
import com.metro.routeplanner.requestdto.BuyTicketRequestDto;
import com.metro.routeplanner.service.TicketService;
import com.metro.routeplanner.util.Constant;
import com.metro.routeplanner.util.QRCodeHelper;
import com.metro.routeplanner.util.UserExtractor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

	private final TicketDao ticketDao;
	private final UserDao userDao;
	private final RouteFinderJGraphT routeFinderJGraphT;
	private final FareCalculatorService fareCalculatorService;

	@Override
	public byte[] buyTicket(BuyTicketRequestDto dto) throws IOException {
		double fare = fareCalculatorService.calculateFare(dto.sourceStation(), dto.destinationStation(),
				dto.paymentMethod());

		User user = userDao.findById(UserExtractor.getUserId())
				.orElseThrow(() -> new NotFoundException(Constant.USER, Constant.USER_NOT_FOUND));

		if (user.getWalletBalance().compareTo(BigDecimal.valueOf(fare)) < 0) {
			throw new NotFoundException(Constant.AMOUNT, Constant.INSUFFICIENT_BALANCE);
		}

		user.setWalletBalance(user.getWalletBalance().subtract(BigDecimal.valueOf(fare)));
		userDao.save(user);

		Ticket ticket = createTicket(dto, fare);
		ticketDao.save(ticket);

		String qrData = QRCodeHelper.generateQRData(ticket);
		return QRCodeHelper.generateQRCode(qrData, 250, 250);
	}

	private Ticket createTicket(BuyTicketRequestDto dto, double fare) {
		return Ticket.builder().sourceStation(dto.sourceStation()).destinationStation(dto.destinationStation())
				.paymentMethod(dto.paymentMethod()).userId(UserExtractor.getUserId()).fare(fare)
				.createdAt(LocalDateTime.now()).expiryTime(LocalDateTime.now().plusHours(1)).isExpired(false)
				.usedAtSource(false).usedAtDestination(false).build();
	}

	@Override
	public boolean processTicket(MultipartFile file, String station, boolean isSource) {
		String extractedData = null;
		try {
			extractedData = QRCodeHelper.extractDataFromQR(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return validateAndUseTicket(extractedData, station, isSource);
	}

	private boolean validateAndUseTicket(String extractedData, String station, boolean isSource) {
		Map<String, String> ticketData = QRCodeHelper.parseQRData(extractedData);
		String ticketId = ticketData.get("Id");

		Ticket ticket = ticketDao.findById(ticketId)
				.orElseThrow(() -> new NotFoundException("Ticket", "Ticket not found"));

		return validateAndUseTicketAtStation(ticket, station, isSource);
	}

	private boolean validateAndUseTicketAtStation(Ticket ticket, String station, boolean isSource) {
		boolean isExpired = ticket.isExpired();
		boolean isAlreadyUsed = isSource ? ticket.isUsedAtSource() : ticket.isUsedAtDestination();

		// Check if station is valid for usage
		boolean isStationAllowed = isSource ? ticket.getSourceStation().equalsIgnoreCase(station)
				: isStationBeforeOrAtDestination(ticket.getSourceStation(), ticket.getDestinationStation(), station);

		boolean isSourceUsedForDestination = !isSource && !ticket.isUsedAtSource();

		// Validation logic for expiration, prior usage, and source usage
		if (isExpired || isAlreadyUsed || isSourceUsedForDestination || !isStationAllowed) {
			return false;
		}

		// Mark ticket as used for the relevant station
		if (isSource) {
			ticket.setUsedAtSource(true);
		} else {
			ticket.setUsedAtDestination(true);
		}

		ticketDao.save(ticket);
		return true;
	}

	/**
	 * Helper method to check if a station is between the source and destination,
	 * inclusive.
	 */
	private boolean isStationBeforeOrAtDestination(String source, String destination, String currentStation) {
		List<String> path = routeFinderJGraphT.findKShortestPaths(source, destination, 1).get(0);
		int currentStationIndex = path.indexOf(currentStation);
		int destinationIndex = path.indexOf(destination);

		return currentStationIndex >= 0 && currentStationIndex <= destinationIndex;
	}

	/**
	 * Scheduled method to delete all expired tickets at 2 AM IST (Delhi Time).
	 */
	@Scheduled(cron = "0 0 2 * * *", zone = "Asia/Kolkata") // 2 AM IST
	@Transactional
	public void deleteExpiredTickets() {
		log.info("Running scheduled task to delete expired tickets.");
		ticketDao.deleteAll();
	}
}
