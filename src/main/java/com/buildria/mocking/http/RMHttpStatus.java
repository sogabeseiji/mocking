package com.buildria.mocking.http;

public final class RMHttpStatus {

    // 1xx
    public static final int SC_100_CONTINUE = 100;
    public static final int SC_101_SWITCHING_PROTOCOLS = 101;
    // 2xx
    public static final int SC_200_OK = 200 ;
    public static final int SC_201_CREATED = 201 ;
    public static final int SC_202_ACCEPTED = 202 ;
    public static final int SC_203_NON_AUTHORITATIVE_INFORMATION = 203 ;
    public static final int SC_204_NOT_CONTENT = 204 ;
    public static final int SC_205_RESET_CONTENT = 205 ;
    public static final int SC_206_PARTIAL_CONTENT = 206 ;
    // 3xx
    public static final int SC_300_MULTIPLE_CHOICES = 300 ;
    public static final int SC_301_MOVED_PERMANENTLY = 301 ;
    public static final int SC_302_MOVED_TEMPORARILY = 302 ;
    public static final int SC_303_SEE_OTHER = 303 ;
    public static final int SC_304_NOT_MODIFIED = 304 ;
    public static final int SC_305_USE_PROXY = 305 ;
    public static final int SC_306_TEMPORARY_REDIRECT = 307 ;
    // 4xx
    public static final int SC_400_BAD_REQUEST = 400 ;
    public static final int SC_401_UNAUTHORIZED = 401 ;
    public static final int SC_402_PAYMENT_REQUIRED = 402 ;
    public static final int SC_403_FORBIDDEN = 403 ;
    public static final int SC_404_NOT_FOUND = 404 ;
    public static final int SC_405_METHOD_NOT_ALLOWED = 405 ;
    public static final int SC_406_NOT_ACCEPTABLE = 406 ;
    public static final int SC_407_PROXY_AUTHENTICATION_REQUIRED = 407 ;
    public static final int SC_408_REQUEST_TIMEOUT = 408 ;
    public static final int SC_409_CONFLICT = 409 ;
    public static final int SC_410_GONE = 410 ;
    public static final int SC_411_LENGTH_REQUIRED = 411 ;
    public static final int SC_412_PRECONDITION_FAILED = 412 ;
    public static final int SC_413_REQUEST_ENTITY_TOO_LARGE = 413 ;
    public static final int SC_414_REQUEST_URI_TOO_LONG = 414 ;
    public static final int SC_415_UNSUPPORTED_MEDIA_TYPE = 415 ;
    public static final int SC_416_REQUESTED_RANGE_NOT_SATISFIABLE = 416 ;
    public static final int SC_417_EXPECTATION_FAILED = 417 ;
    // 5xx
    public static final int SC_500_INTERNAL_SERVER_ERROR = 500 ;
    public static final int SC_501_NOT_IMPLEMENTED = 501 ;
    public static final int SC_502_BAD_GATEWAY = 502 ;
    public static final int SC_503_SERVICE_UNAVAILABLE = 503 ;
    public static final int SC_504_GATEWAY_TIMEOUT = 504 ;
    public static final int SC_505_HTTP_VERSION_NOT_SUPPORTED = 505;

    private RMHttpStatus() {
        super();
    }

}
