package com.metro.routeplanner.util;

import lombok.Data;

@Data
public class Constant {

	// General Constants
	public static final String STATION_ID = "StationId";
	public static final String LINE_NAME = "lineName";
	public static final String STATION = "station";

	// Error messages
	public static final String STATION_NOT_FOUND = "Station not found";
	public static final String LINE_NOT_FOUND = "Line not found";
	public static final String NO_COMMON_LINE_FOUND = "No common line found between stations.";
	public static final String NO_VALID_INTERCHANGE = "No valid interchange found.";

	public static final String STATION_ADDED_SUCCESS = "Station added successfully";
	public static final String STATION_UPDATED_SUCCESS = "Station updated successfully";
	public static final String STATION_FETCHED_SUCCESS = "Station fetched successfully";
	public static final String STATIONS_FETCHED_SUCCESS = "Stations fetched successfully";
	public static final String LINE_ADDED_SUCCESS = "Line added successfully";
	public static final String STATIONS_ADDED_SUCCESS = "Stations added successfully";
	public static final String ROUTE_FETCHED_SUCCESS = "Route fetched successfully";
	public static final String TICKET_PURCHASE_SUCCESS = "Ticket purchased successfully";
	public static final String TICKET_PURCHASE_FAIL = "Ticket purchase failed";

	public static final String USER = "User";
	public static final String USER_NOT_FOUND = "User not found";
	public static final String USER_ADDED_SUCCESS = "User added successfully";
	public static final String USER_UPDATED_SUCCESS = "User updated successfully";
	public static final String WALLET_BALANCE_UPDATED = "Wallet balance updated successfully";
	public static final String LOGIN_SUCCESS = "Login successfully";
	public static final String LOGIN_FAILED = "Cannot Login, Try again!";
	public static final String USER_ALREADY_EXIST = "User with this email already exists";
	public static final String EMAIL = "Email";
	public static final String INSUFFICIENT_BALANCE = "Insufficient balance to buy the ticket.";
	public static final String AMOUNT = "Amount";

	public static final String TICKET_USED_SOURCE_SUCCESS = "Ticket used successfully at source station.";
	public static final String TICKET_USED_SOURCE_FAILURE = "Failed to use ticket at source station.";

	public static final String TICKET_USED_DEST_SUCCESS = "Ticket used successfully at destination station.";
	public static final String TICKET_USED_DEST_FAILURE = "Failed to use ticket at destination station.";
	
	// QR Code related constants
    public static final String QR_CODE_GENERATION_FAILED = "Failed to generate QR Code: ";
    public static final String QR_CODE_NOT_FOUND = "QR Code not found in image";

    // QR Code data format constants
    public static final String QR_DATA_FORMAT = "Id: %s, UserId: %s, Source: %s, Destination: %s, Payment: %s, CreatedAt: %s, Expiry: %s, Fare: %s, UsedAtSource: %s, UsedAtDestination: %s";

    // Parsing QR Code data constants
    public static final String QR_DATA_DELIMITER = ", ";
    public static final String QR_KEY_VALUE_SEPARATOR = ": ";
	public static final String NO_VALID_ROUTE_FOUND = "No route";

}
