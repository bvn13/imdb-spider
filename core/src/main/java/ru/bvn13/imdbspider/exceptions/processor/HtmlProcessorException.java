package ru.bvn13.imdbspider.exceptions.processor;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;

/**
 * @author boyko_vn at 09.01.2019
 */
public class HtmlProcessorException extends ImdbSpiderException {

    public HtmlProcessorException() {
    }

    public HtmlProcessorException(String message) {
        super(message);
    }

    public HtmlProcessorException(String message, Throwable cause) {
        super(message, cause);
    }

    public HtmlProcessorException(Throwable cause) {
        super(cause);
    }

    public HtmlProcessorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
