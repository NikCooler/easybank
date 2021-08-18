/*
 * This file is generated by jOOQ.
 */
package org.craftedsw.model.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.type.Currency;
import org.craftedsw.type.TransactionStatus;
import org.craftedsw.type.TransactionType;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Transaction implements Serializable {

    private static final long serialVersionUID = -564118590;

    private TransactionId     transactionId;
    private UserId            transferredFrom;
    private UserId            transferredTo;
    private TransactionStatus status;
    private TransactionType   type;
    private Currency          currency;
    private BigDecimal        value;
    private String            errorMessage;
    private Long              eventTimestamp;

    public Transaction() {}

    public Transaction(Transaction value) {
        this.transactionId = value.transactionId;
        this.transferredFrom = value.transferredFrom;
        this.transferredTo = value.transferredTo;
        this.status = value.status;
        this.type = value.type;
        this.currency = value.currency;
        this.value = value.value;
        this.errorMessage = value.errorMessage;
        this.eventTimestamp = value.eventTimestamp;
    }

    public Transaction(
        TransactionId     transactionId,
        UserId            transferredFrom,
        UserId            transferredTo,
        TransactionStatus status,
        TransactionType   type,
        Currency          currency,
        BigDecimal        value,
        String            errorMessage,
        Long              eventTimestamp
    ) {
        this.transactionId = transactionId;
        this.transferredFrom = transferredFrom;
        this.transferredTo = transferredTo;
        this.status = status;
        this.type = type;
        this.currency = currency;
        this.value = value;
        this.errorMessage = errorMessage;
        this.eventTimestamp = eventTimestamp;
    }

    public TransactionId getTransactionId() {
        return this.transactionId;
    }

    public Transaction setTransactionId(TransactionId transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public UserId getTransferredFrom() {
        return this.transferredFrom;
    }

    public Transaction setTransferredFrom(UserId transferredFrom) {
        this.transferredFrom = transferredFrom;
        return this;
    }

    public UserId getTransferredTo() {
        return this.transferredTo;
    }

    public Transaction setTransferredTo(UserId transferredTo) {
        this.transferredTo = transferredTo;
        return this;
    }

    public TransactionStatus getStatus() {
        return this.status;
    }

    public Transaction setStatus(TransactionStatus status) {
        this.status = status;
        return this;
    }

    public TransactionType getType() {
        return this.type;
    }

    public Transaction setType(TransactionType type) {
        this.type = type;
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public Transaction setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public Transaction setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public Transaction setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public Long getEventTimestamp() {
        return this.eventTimestamp;
    }

    public Transaction setEventTimestamp(Long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Transaction other = (Transaction) obj;
        if (transactionId == null) {
            if (other.transactionId != null)
                return false;
        }
        else if (!transactionId.equals(other.transactionId))
            return false;
        if (transferredFrom == null) {
            if (other.transferredFrom != null)
                return false;
        }
        else if (!transferredFrom.equals(other.transferredFrom))
            return false;
        if (transferredTo == null) {
            if (other.transferredTo != null)
                return false;
        }
        else if (!transferredTo.equals(other.transferredTo))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        }
        else if (!status.equals(other.status))
            return false;
        if (type == null) {
            if (other.type != null)
                return false;
        }
        else if (!type.equals(other.type))
            return false;
        if (currency == null) {
            if (other.currency != null)
                return false;
        }
        else if (!currency.equals(other.currency))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        if (errorMessage == null) {
            if (other.errorMessage != null)
                return false;
        }
        else if (!errorMessage.equals(other.errorMessage))
            return false;
        if (eventTimestamp == null) {
            if (other.eventTimestamp != null)
                return false;
        }
        else if (!eventTimestamp.equals(other.eventTimestamp))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.transactionId == null) ? 0 : this.transactionId.hashCode());
        result = prime * result + ((this.transferredFrom == null) ? 0 : this.transferredFrom.hashCode());
        result = prime * result + ((this.transferredTo == null) ? 0 : this.transferredTo.hashCode());
        result = prime * result + ((this.status == null) ? 0 : this.status.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        result = prime * result + ((this.currency == null) ? 0 : this.currency.hashCode());
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
        result = prime * result + ((this.errorMessage == null) ? 0 : this.errorMessage.hashCode());
        result = prime * result + ((this.eventTimestamp == null) ? 0 : this.eventTimestamp.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Transaction (");

        sb.append(transactionId);
        sb.append(", ").append(transferredFrom);
        sb.append(", ").append(transferredTo);
        sb.append(", ").append(status);
        sb.append(", ").append(type);
        sb.append(", ").append(currency);
        sb.append(", ").append(value);
        sb.append(", ").append(errorMessage);
        sb.append(", ").append(eventTimestamp);

        sb.append(")");
        return sb.toString();
    }
}