package org.craftedsw.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * @author Nikolay Smirnov
 */
public class Transaction {

    private final TransactionState state;
    private final Transfer transfer;

    private Transaction(TransactionState state, Transfer transfer) {
        this.state = state;
        this.transfer = transfer;
    }

    @JsonCreator
    public static Transaction of(
            @JsonProperty("state") TransactionState state,
            @JsonProperty("transfer") Transfer transfer
    ) {
        return new Transaction(state, transfer);
    }

    public void transitToStatus(TransactionStatus status) {
        state.transitTo(status);
    }

    public TransactionStatus getStatus() {
        return state.getStatus();
    }

    public Transfer getTransfer() {
        return transfer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(state, that.state) && Objects.equals(transfer, that.transfer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, transfer);
    }
}
