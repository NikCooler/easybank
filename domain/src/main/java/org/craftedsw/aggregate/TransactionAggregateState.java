package org.craftedsw.aggregate;

import org.craftedsw.event.TransactionCompletedEvent;
import org.craftedsw.event.TransactionFailedEvent;
import org.craftedsw.event.TransactionStartedEvent;
import org.craftedsw.type.*;

/**
 * @author Nikolay Smirnov
 */
public class TransactionAggregateState extends AggregateStateBase<TransactionId> {

    private Transaction transaction;
    private Amount amount;
    private String errorMessage;

    TransactionAggregateState(TransactionId aggregateId) {
        super(aggregateId);
    }

    public void apply(TransactionStartedEvent event) {
        transaction = event.getTransaction();
        amount = event.getAmount();
    }

    public void apply(TransactionCompletedEvent event) {
        transaction.transitToStatus(TransactionStatus.COMPLETED);
    }

    public void apply(TransactionFailedEvent event) {
        errorMessage = event.getErrorMessage();
        transaction.transitToStatus(TransactionStatus.COMPLETED);
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
