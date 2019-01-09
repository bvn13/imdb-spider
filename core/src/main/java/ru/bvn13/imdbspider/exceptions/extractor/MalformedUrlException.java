package ru.bvn13.imdbspider.exceptions.extractor;


/**
 * @author boyko_vn at 09.01.2019
 */
public class MalformedUrlException extends HtmlExtractorException {

    public MalformedUrlException() {
    }

    public MalformedUrlException(String message) {
        super(message);
    }

    public MalformedUrlException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedUrlException(Throwable cause) {
        super(cause);
    }

    public MalformedUrlException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
