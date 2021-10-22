package org.easybank.cqrs.query;

/**
 * An exception to track error messages in {@link QueryProcessorBase}
 *
 * @author Nikolay Smirnov
 */
public class QueryValidationException extends RuntimeException {

    public QueryValidationException(String message) {
        super(message);
    }
}
