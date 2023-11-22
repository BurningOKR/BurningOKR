package org.burningokr.service.exceptions;

public class AuthorizationHeaderException extends RuntimeException {
    public AuthorizationHeaderException(String message) {
        super(message);
    }
}
