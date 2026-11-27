package org.example.exception;

public class InvalidBookingDateException extends RuntimeException {
    public InvalidBookingDateException(String message) {
        super(message);
    }
    public InvalidBookingDateException(String message, Throwable cause) {
        super(message, cause);
    }
}
