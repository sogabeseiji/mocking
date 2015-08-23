package com.buildria.restmock.http;

public enum HttpStatus {

    // 20X
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    NOT_CONTENT(204);

    private final int code;

    private HttpStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
