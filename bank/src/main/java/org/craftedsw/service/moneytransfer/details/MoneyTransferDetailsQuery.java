package org.craftedsw.service.moneytransfer.details;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.cqrs.query.Query;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferDetailsQuery implements Query {

    private final TransactionId transactionId;

    public MoneyTransferDetailsQuery(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }
}
