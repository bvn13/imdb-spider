package ru.bvn13.imdbspider.exceptions.api;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;

/**
 * @author boyko_vn at 09.01.2019
 */
public class DataTypeNotSupportedException extends ImdbSpiderException {

    public DataTypeNotSupportedException() {
    }

    public DataTypeNotSupportedException(String message) {
        super(message);
    }

    public DataTypeNotSupportedException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataTypeNotSupportedException(Throwable cause) {
        super(cause);
    }

    public DataTypeNotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
