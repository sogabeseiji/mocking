package com.buildria.mocking;

public class MockingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MockingException() {
        super();
    }

    public MockingException(String message) {
        super(message);
    }

    public MockingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MockingException(Throwable cause) {
        super(cause);
    }

    public MockingException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
