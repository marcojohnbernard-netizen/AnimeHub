package com.animehub.exception;

/**
 * Itinatapon kapag bigo ang pagtawag sa Jikan API (timeout, 404, rate limit,
 * network error, atbp). Hinuhuli ito sa service layer para makapagbigay
 * tayo ng malinaw na fallback sa user imbes na biglang error page lang.
 */
public class JikanApiException extends RuntimeException {
    public JikanApiException(String message) {
        super(message);
    }

    public JikanApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
