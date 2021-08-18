package org.craftedsw.aggregate;

import org.craftedsw.event.MoneyTransferCompletedEvent;
import org.craftedsw.event.MoneyTransferFailedEvent;
import org.craftedsw.event.MoneyTransferStartedEvent;
import org.craftedsw.type.Currency;
import org.craftedsw.type.TransferStatus;

import java.math.BigDecimal;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferAggregateState extends AggregateStateBase<TransactionId> {

    private UserId transferFrom;
    private UserId transferTo;
    private Currency currency;
    private BigDecimal value;
    private String errorMessage;
    private TransferStatus transferStatus;

    MoneyTransferAggregateState(TransactionId aggregateId) {
        super(aggregateId);
    }

    public void apply(MoneyTransferStartedEvent event) {
        this.transferFrom = event.getTransferFrom();
        this.transferTo = event.getTransferTo();
        this.currency = event.getCurrency();
        this.value = event.getValue();
        this.transferStatus = TransferStatus.PROCESSING;
    }

    public void apply(MoneyTransferCompletedEvent event) {
        this.transferStatus = TransferStatus.COMPLETED;
    }

    public void apply(MoneyTransferFailedEvent event) {
        this.errorMessage = event.getErrorMessage();
        this.transferStatus = TransferStatus.FAILED;
    }

    public UserId getTransferFrom() {
        return transferFrom;
    }

    public UserId getTransferTo() {
        return transferTo;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public TransferStatus getTransferStatus() {
        return transferStatus;
    }
}
