package org.craftedsw.dto;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.type.Currency;
import org.craftedsw.type.TransactionStatus;

import java.math.BigDecimal;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferDetailsDto {

    private final TransactionId transactionId;

    private UserId transferFrom;
    private UserId transferTo;
    private TransactionStatus transactionStatus;
    private Currency currency;
    private BigDecimal value;

    public MoneyTransferDetailsDto(TransactionId transactionId) {
        this.transactionId = transactionId;
    }

    public UserId getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(UserId transferFrom) {
        this.transferFrom = transferFrom;
    }

    public UserId getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(UserId transferTo) {
        this.transferTo = transferTo;
    }

    public TransactionStatus getTransferStatus() {
        return transactionStatus;
    }

    public void setTransferStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public TransactionId getTransactionId() {
        return transactionId;
    }
}
