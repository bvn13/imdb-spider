package ru.bvn13.imdbspider.exceptions;

/**
 * @author boyko_vn at 09.01.2019
 */
public class ImdbSpiderException extends Exception {

    public ImdbSpiderException() {
    }

    public ImdbSpiderException(String message) {
        super(message);
    }

    public ImdbSpiderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImdbSpiderException(Throwable cause) {
        super(cause);
    }

    public ImdbSpiderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
