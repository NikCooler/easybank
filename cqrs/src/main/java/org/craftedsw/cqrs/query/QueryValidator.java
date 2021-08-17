package org.craftedsw.cqrs.query;

/**
 * @author Nikolay Smirnov
 */
public interface QueryValidator<Q> {

    boolean isValid(Q query);

}
