package org.craftedsw.cqrs.query;

import org.craftedsw.cqrs.HttpCode;
import org.craftedsw.cqrs.Response;
import org.jooq.DSLContext;

/**
 * Read lane.
 * Retrieve all needed data from the Read Model
 *
 * @author Nikolay Smirnov
 */
public abstract class QueryProcessorBase<Q extends Query, DTO> {

    protected final DSLContext      readModelContext;
    private final QueryValidator<Q> queryValidator;

    protected QueryProcessorBase(DSLContext readModelContext, QueryValidator<Q> queryValidator) {
        this.readModelContext = readModelContext;
        this.queryValidator   = queryValidator;
    }

    public Response executeQuery(Q query) {
        try {
            if (!queryValidator.isValid(query)) {
                throw new QueryValidationException("Query is not valid!");
            }

            return Response.of(HttpCode.OK, buildPayload(query));
        } catch (Exception ex) {
            return Response.of(HttpCode.BAD_REQUEST.getCode(), ex.getMessage());
        }
    }

    protected abstract DTO buildPayload(Q query);

}
