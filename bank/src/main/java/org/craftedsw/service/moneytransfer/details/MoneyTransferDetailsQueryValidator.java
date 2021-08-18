package org.craftedsw.service.moneytransfer.details;

import org.craftedsw.cqrs.query.QueryValidationException;
import org.craftedsw.cqrs.query.QueryValidator;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import static org.craftedsw.model.tables.MoneyTransfer.MONEY_TRANSFER;

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
                        .from(MONEY_TRANSFER)
                        .where(MONEY_TRANSFER.TRANSACTION_ID.eq(query.getTransactionId())));
        if (!moneyTransferExists) {
            throw new QueryValidationException("Money transfer doesn't exist by id [ " + query.getTransactionId() + " ]");
        }
        return true;
    }
}
