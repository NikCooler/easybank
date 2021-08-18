package org.craftedsw.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Nikolay Smirnov
 */
public class TransactionState {

    private final TransactionType type;
    private TransactionStatus status;

    private TransactionState(TransactionType type, TransactionStatus status) {
        this.type = type;
        this.status = status;
    }

    @JsonCreator
    public static TransactionState of(
            @JsonProperty("type") TransactionType type,
            @JsonProperty("status") TransactionStatus status
    ) {
        return new TransactionState(type, status);
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void transitTo(TransactionStatus newStatus) {
        this.status = newStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionState that = (TransactionState) o;
        return type == that.type && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, status);
    }
}
