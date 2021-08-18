package org.craftedsw.readlane;

import org.craftedsw.event.*;
import org.craftedsw.model.tables.records.EventStoreRecord;
import org.craftedsw.type.TransferStatus;
import org.craftedsw.writelane.eventbus.EventStoredIdSubscriber;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.lang.reflect.Method;

import static org.craftedsw.model.tables.EventStore.EVENT_STORE;
import static org.craftedsw.model.tables.MoneyAccount.MONEY_ACCOUNT;
import static org.craftedsw.model.tables.MoneyTransfer.MONEY_TRANSFER;
import static org.craftedsw.model.tables.User.USER;
import static org.craftedsw.util.EventDeserializer.deserialize;

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
        readModelContext.insertInto(MONEY_ACCOUNT)
                .set(MONEY_ACCOUNT.USER_ID, event.getAggregateId())
                .set(MONEY_ACCOUNT.CURRENCY, event.getMoneyAccount().getCurrency())
                .set(MONEY_ACCOUNT.VALUE, event.getMoneyAccount().getValue())
                .execute();
    }

    public void apply(MoneyTransferStartedEvent event) {
        readModelContext.insertInto(MONEY_TRANSFER)
                .set(MONEY_TRANSFER.TRANSACTION_ID, event.getAggregateId())
                .set(MONEY_TRANSFER.TRANSFERRED_FROM, event.getTransferFrom())
                .set(MONEY_TRANSFER.TRANSFERRED_TO, event.getTransferTo())
                .set(MONEY_TRANSFER.STATUS, TransferStatus.PROCESSING)
                .set(MONEY_TRANSFER.CURRENCY, event.getCurrency())
                .set(MONEY_TRANSFER.VALUE, event.getValue())
                .execute();
    }

    public void apply(MoneyTransferCompletedEvent event) {
        readModelContext.update(MONEY_TRANSFER)
                .set(MONEY_TRANSFER.STATUS, TransferStatus.COMPLETED)
                .where(MONEY_TRANSFER.TRANSACTION_ID.eq(event.getAggregateId()))
                .execute();
    }

    public void apply(MoneyTransferFailedEvent event) {
        readModelContext.update(MONEY_TRANSFER)
                .set(MONEY_TRANSFER.STATUS, TransferStatus.FAILED)
                .set(MONEY_TRANSFER.ERROR_MESSAGE, event.getErrorMessage())
                .where(MONEY_TRANSFER.TRANSACTION_ID.eq(event.getAggregateId()))
                .execute();
    }

    public void apply(MoneyWithdrawnEvent event) {
        readModelContext.update(MONEY_ACCOUNT)
                .set(MONEY_ACCOUNT.VALUE, MONEY_ACCOUNT.VALUE.minus(event.getValue()))
                .where(DSL.and(
                        MONEY_ACCOUNT.USER_ID.eq(event.getAggregateId()),
                        MONEY_ACCOUNT.CURRENCY.eq(event.getCurrency())
                ))
                .execute();
    }

    public void apply(MoneyToppedUpEvent event) {
        readModelContext.update(MONEY_ACCOUNT)
                .set(MONEY_ACCOUNT.VALUE, MONEY_ACCOUNT.VALUE.plus(event.getValue()))
                .where(DSL.and(
                        MONEY_ACCOUNT.USER_ID.eq(event.getAggregateId()),
                        MONEY_ACCOUNT.CURRENCY.eq(event.getCurrency())
                ))
                .execute();
    }

}
