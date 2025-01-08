package com.metro.routeplanner.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.metro.routeplanner.requestdto.BuyTicketRequestDto;

public interface TicketService {

	byte[] buyTicket(BuyTicketRequestDto dto) throws IOException;

	boolean processTicket(MultipartFile file, String station, boolean isSource);

}
