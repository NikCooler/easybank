package org.easybank.service.moneytransfer.details;

import org.easybank.cqrs.query.QueryValidationException;
import org.easybank.cqrs.query.QueryValidator;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import static java.lang.String.format;
import static org.easybank.model.tables.Transaction.TRANSACTION;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferDetailsQueryValidator implements QueryValidator<MoneyTransferDetailsQuery> {

    private final DSLContext readModelContext;

    public MoneyTransferDetailsQueryValidator(DSLContext readModelContext) {
        this.readModelContext = readModelContext;
    }

    @Override
    public boolean isValid(MoneyTransferDetailsQuery query) {
        boolean moneyTransferExists = readModelContext.fetchExists(
                DSL.select(DSL.one())
                        .from(TRANSACTION)
                        .where(TRANSACTION.TRANSACTION_ID.eq(query.getTransactionId())));
        if (!moneyTransferExists) {
            throw new QueryValidationException(format("Money transfer doesn't exist by id [ %s ]", query.getTransactionId()));
        }
        return true;
    }
}
