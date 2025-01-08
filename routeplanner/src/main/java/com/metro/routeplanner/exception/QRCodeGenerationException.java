package com.metro.routeplanner.exception;

import org.springframework.http.HttpStatus;

public class QRCodeGenerationException extends GenericException {

    private static final long serialVersionUID = 1L;

    public QRCodeGenerationException(String message) {
        super("QRCode Generation", HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
