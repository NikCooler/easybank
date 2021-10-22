package org.easybank.service.moneytransfer.details;

import org.easybank.aggregate.TransactionId;
import org.easybank.cqrs.query.Query;

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
