package ru.bvn13.imdbspider.exceptions.processor;

/**
 * @author boyko_vn at 09.01.2019
 */
public class PatternEvaluationException extends HtmlProcessorException {

    public PatternEvaluationException() {
    }

    public PatternEvaluationException(String message) {
        super(message);
    }

    public PatternEvaluationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PatternEvaluationException(Throwable cause) {
        super(cause);
    }

    public PatternEvaluationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
