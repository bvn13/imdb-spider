package ru.bvn13.imdbspider.exceptions.composer;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;

/**
 * @author boyko_vn at 10.01.2019
 */
public class ComposerNotFoundException extends ImdbSpiderException {

    public ComposerNotFoundException() {
    }

    public ComposerNotFoundException(String message) {
        super(message);
    }

    public ComposerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ComposerNotFoundException(Throwable cause) {
        super(cause);
    }

    public ComposerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
