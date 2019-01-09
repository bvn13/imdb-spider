package ru.bvn13.imdbspider.exceptions.extractor;

import ru.bvn13.imdbspider.exceptions.ImdbSpiderException;

/**
 * @author boyko_vn at 09.01.2019
 */
public class HtmlExtractorException extends ImdbSpiderException {
    public HtmlExtractorException() {
    }

    public HtmlExtractorException(String message) {
        super(message);
    }

    public HtmlExtractorException(String message, Throwable cause) {
        super(message, cause);
    }

    public HtmlExtractorException(Throwable cause) {
        super(cause);
    }

    public HtmlExtractorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
