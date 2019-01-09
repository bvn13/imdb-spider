package ru.bvn13.imdbspider.exceptions.extractor;

/**
 * @author boyko_vn at 09.01.2019
 */
public class ConnectionEstablishingException extends HtmlExtractorException {

    public ConnectionEstablishingException() {
    }

    public ConnectionEstablishingException(String message) {
        super(message);
    }

    public ConnectionEstablishingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionEstablishingException(Throwable cause) {
        super(cause);
    }

    public ConnectionEstablishingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
