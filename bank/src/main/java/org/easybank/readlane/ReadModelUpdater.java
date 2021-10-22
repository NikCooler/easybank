package org.easybank.readlane;

import org.easybank.event.*;
import org.easybank.model.tables.records.EventStoreRecord;
import org.easybank.writelane.eventbus.EventStoredIdSubscriber;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.lang.reflect.Method;

import static org.easybank.model.tables.EventStore.EVENT_STORE;
import static org.easybank.model.tables.MoneyAccount.MONEY_ACCOUNT;
import static org.easybank.model.tables.Transaction.TRANSACTION;
import static org.easybank.model.tables.User.USER;
import static org.easybank.util.EventDeserializer.deserialize;

/**
 * @author Nikolay Smirnov
 */
public class ReadModelUpdater implements EventStoredIdSubscriber {

    private final DSLContext readModelContext;

    public ReadModelUpdater(DSLContext readModelContext) {
        this.readModelContext = readModelContext;
    }

    @Override
    public void onSubscribe(Long eventStoredId) {
        readModelContext.transaction(conf -> {
            var txContext = DSL.using(conf);
            EventStoreRecord eventRec = txContext.selectFrom(EVENT_STORE)
                    .where(EVENT_STORE.EVENT_ID.eq(eventStoredId))
                    .fetchOne();

            EventBase<?> event = deserialize(eventRec);
            Method apply = this.getClass().getMethod("apply", event.getClass());
            apply.invoke(this, event);
        });
    }

    public void apply(UserRegisteredEvent event) {
        readModelContext.insertInto(USER)
                .set(USER.USER_ID, event.getAggregateId())
                .set(USER.EMAIL, event.getEmail())
                .execute();
    }

    public void apply(MoneyAccountCreatedEvent event) {
        var moneyAccount = event.getMoneyAccount();
        readModelContext.insertInto(MONEY_ACCOUNT)
                .set(MONEY_ACCOUNT.USER_ID, event.getAggregateId())
                .set(MONEY_ACCOUNT.CURRENCY, moneyAccount.getCurrency())
                .set(MONEY_ACCOUNT.VALUE, moneyAccount.getValue())
                .execute();
    }

    public void apply(TransactionStartedEvent event) {
        var amount = event.getAmount();
        var transaction = event.getTransaction();
        var transfer = transaction.getTransfer();
        readModelContext.insertInto(TRANSACTION)
                .set(TRANSACTION.TRANSACTION_ID, event.getAggregateId())
                .set(TRANSACTION.TRANSFERRED_FROM, transfer.getWithdrawFrom())
                .set(TRANSACTION.TRANSFERRED_TO, transfer.getDepositTo())
                .set(TRANSACTION.STATUS, transaction.getStatus())
                .set(TRANSACTION.TYPE, transaction.getType())
                .set(TRANSACTION.CURRENCY, amount.getCurrency())
                .set(TRANSACTION.VALUE, amount.getValue())
                .set(TRANSACTION.EVENT_TIMESTAMP, event.getEventTimestamp())
                .execute();
    }

    public void apply(TransactionCompletedEvent event) {
        readModelContext.update(TRANSACTION)
                .set(TRANSACTION.STATUS, event.getStatus())
                .where(TRANSACTION.TRANSACTION_ID.eq(event.getAggregateId()))
                .execute();
    }

    public void apply(TransactionFailedEvent event) {
        readModelContext.update(TRANSACTION)
                .set(TRANSACTION.STATUS, event.getStatus())
                .set(TRANSACTION.ERROR_MESSAGE, event.getErrorMessage())
                .where(TRANSACTION.TRANSACTION_ID.eq(event.getAggregateId()))
                .execute();
    }

    public void apply(WithdrawnEvent event) {
        var withdrawAmount = event.getAmount();
        readModelContext.update(MONEY_ACCOUNT)
                .set(MONEY_ACCOUNT.VALUE, MONEY_ACCOUNT.VALUE.minus(withdrawAmount.getValue()))
                .where(DSL.and(
                        MONEY_ACCOUNT.USER_ID.eq(event.getAggregateId()),
                        MONEY_ACCOUNT.CURRENCY.eq(withdrawAmount.getCurrency())
                ))
                .execute();
    }

    public void apply(DepositEvent event) {
        var depositAmount = event.getAmount();
        readModelContext.update(MONEY_ACCOUNT)
                .set(MONEY_ACCOUNT.VALUE, MONEY_ACCOUNT.VALUE.plus(depositAmount.getValue()))
                .where(DSL.and(
                        MONEY_ACCOUNT.USER_ID.eq(event.getAggregateId()),
                        MONEY_ACCOUNT.CURRENCY.eq(depositAmount.getCurrency())
                ))
                .execute();
    }
}
