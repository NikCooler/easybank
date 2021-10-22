package org.easybank.cqrs.command;

/**
 * An exception to track error messages in {@link CommandProcessorBase}
 *
 * @author Nikolay Smirnov
 */
public class CommandValidationException extends RuntimeException {

    public CommandValidationException(String message) {
        super(message);
    }
}
