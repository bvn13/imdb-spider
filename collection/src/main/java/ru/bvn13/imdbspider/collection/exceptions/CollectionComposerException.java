package ru.bvn13.imdbspider.collection.exceptions;

/**
 * Created by bvn13 on 28.01.2019.
 */
public class CollectionComposerException extends Exception {
    public CollectionComposerException() {
    }

    public CollectionComposerException(String message) {
        super(message);
    }

    public CollectionComposerException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollectionComposerException(Throwable cause) {
        super(cause);
    }

    public CollectionComposerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
