package org.craftedsw.aggregate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jooq.Converter;

import java.util.UUID;

/**
 * @author Nikolay Smirnov
 */
public class TransactionId extends AggregateIdBase<TransactionId> {

    public TransactionId(UUID transactionId) {
        super(transactionId);
    }

    public TransactionId(String transactionId) {
        super(UUID.fromString(transactionId));
    }

    @Override
    public AggregateStateBase<TransactionId> newInstanceState() {
        return new MoneyTransferAggregateState(this);
    }

    @JsonCreator
    public static TransactionId valueOf(@JsonProperty("transactionId") String transactionId) {
        return new TransactionId(transactionId);
    }

    public static TransactionId random() {
        return new TransactionId(UUID.randomUUID());
    }

    public static class JooqTransactionIdConverter implements Converter<String, TransactionId> {
        private static final long serialVersionUID = 1L;

        @Override
        public TransactionId from(String s) {
            return TransactionId.valueOf(s);
        }

        @Override
        public String to(TransactionId transactionId) {
            return transactionId.toString();
        }

        @Override
        public Class<String> fromType() {
            return String.class;
        }

        @Override
        public Class<TransactionId> toType() {
            return TransactionId.class;
        }
    }
}
