package ru.bvn13.imdbspider.exceptions.processor;

/**
 * @author boyko_vn at 09.01.2019
 */
public class HtmlToXmlConvertionException extends HtmlProcessorException {

    public HtmlToXmlConvertionException() {
    }

    public HtmlToXmlConvertionException(String message) {
        super(message);
    }

    public HtmlToXmlConvertionException(String message, Throwable cause) {
        super(message, cause);
    }

    public HtmlToXmlConvertionException(Throwable cause) {
        super(cause);
    }

    public HtmlToXmlConvertionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
