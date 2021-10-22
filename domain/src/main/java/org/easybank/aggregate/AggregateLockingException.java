package org.easybank.aggregate;

/**
 * @author Nikolay Smirnov
 */
public class AggregateLockingException extends RuntimeException {

    public AggregateLockingException(String message) {
        super(message);
    }
}
