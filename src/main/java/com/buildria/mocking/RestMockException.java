package com.buildria.mocking;

public class RestMockException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RestMockException() {
        super();
    }

    public RestMockException(String message) {
        super(message);
    }

    public RestMockException(String message, Throwable cause) {
        super(message, cause);
    }

    public RestMockException(Throwable cause) {
        super(cause);
    }

    public RestMockException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
