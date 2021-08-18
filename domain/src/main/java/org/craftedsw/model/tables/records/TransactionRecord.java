/*
 * This file is generated by jOOQ.
 */
package org.craftedsw.model.tables.records;


import java.math.BigDecimal;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.model.tables.Transaction;
import org.craftedsw.type.Currency;
import org.craftedsw.type.TransactionStatus;
import org.craftedsw.type.TransactionType;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record9;
import org.jooq.Row9;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class TransactionRecord extends UpdatableRecordImpl<TransactionRecord> implements Record9<TransactionId, UserId, UserId, TransactionStatus, TransactionType, Currency, BigDecimal, String, Long> {

    private static final long serialVersionUID = -2055865321;

    /**
     * Setter for <code>TRANSACTION.TRANSACTION_ID</code>.
     */
    public TransactionRecord setTransactionId(TransactionId value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>TRANSACTION.TRANSACTION_ID</code>.
     */
    public TransactionId getTransactionId() {
        return (TransactionId) get(0);
    }

    /**
     * Setter for <code>TRANSACTION.TRANSFERRED_FROM</code>.
     */
    public TransactionRecord setTransferredFrom(UserId value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>TRANSACTION.TRANSFERRED_FROM</code>.
     */
    public UserId getTransferredFrom() {
        return (UserId) get(1);
    }

    /**
     * Setter for <code>TRANSACTION.TRANSFERRED_TO</code>.
     */
    public TransactionRecord setTransferredTo(UserId value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>TRANSACTION.TRANSFERRED_TO</code>.
     */
    public UserId getTransferredTo() {
        return (UserId) get(2);
    }

    /**
     * Setter for <code>TRANSACTION.STATUS</code>.
     */
    public TransactionRecord setStatus(TransactionStatus value) {
        set(3, value);
        return this;
    }

    /**
     * Getter for <code>TRANSACTION.STATUS</code>.
     */
    public TransactionStatus getStatus() {
        return (TransactionStatus) get(3);
    }

    /**
     * Setter for <code>TRANSACTION.TYPE</code>.
     */
    public TransactionRecord setType(TransactionType value) {
        set(4, value);
        return this;
    }

    /**
     * Getter for <code>TRANSACTION.TYPE</code>.
     */
    public TransactionType getType() {
        return (TransactionType) get(4);
    }

    /**
     * Setter for <code>TRANSACTION.CURRENCY</code>.
     */
    public TransactionRecord setCurrency(Currency value) {
        set(5, value);
        return this;
    }

    /**
     * Getter for <code>TRANSACTION.CURRENCY</code>.
     */
    public Currency getCurrency() {
        return (Currency) get(5);
    }

    /**
     * Setter for <code>TRANSACTION.VALUE</code>.
     */
    public TransactionRecord setValue(BigDecimal value) {
        set(6, value);
        return this;
    }

    /**
     * Getter for <code>TRANSACTION.VALUE</code>.
     */
    public BigDecimal getValue() {
        return (BigDecimal) get(6);
    }

    /**
     * Setter for <code>TRANSACTION.ERROR_MESSAGE</code>.
     */
    public TransactionRecord setErrorMessage(String value) {
        set(7, value);
        return this;
    }

    /**
     * Getter for <code>TRANSACTION.ERROR_MESSAGE</code>.
     */
    public String getErrorMessage() {
        return (String) get(7);
    }

    /**
     * Setter for <code>TRANSACTION.EVENT_TIMESTAMP</code>.
     */
    public TransactionRecord setEventTimestamp(Long value) {
        set(8, value);
        return this;
    }

    /**
     * Getter for <code>TRANSACTION.EVENT_TIMESTAMP</code>.
     */
    public Long getEventTimestamp() {
        return (Long) get(8);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<TransactionId> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record9 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row9<TransactionId, UserId, UserId, TransactionStatus, TransactionType, Currency, BigDecimal, String, Long> fieldsRow() {
        return (Row9) super.fieldsRow();
    }

    @Override
    public Row9<TransactionId, UserId, UserId, TransactionStatus, TransactionType, Currency, BigDecimal, String, Long> valuesRow() {
        return (Row9) super.valuesRow();
    }

    @Override
    public Field<TransactionId> field1() {
        return Transaction.TRANSACTION.TRANSACTION_ID;
    }

    @Override
    public Field<UserId> field2() {
        return Transaction.TRANSACTION.TRANSFERRED_FROM;
    }

    @Override
    public Field<UserId> field3() {
        return Transaction.TRANSACTION.TRANSFERRED_TO;
    }

    @Override
    public Field<TransactionStatus> field4() {
        return Transaction.TRANSACTION.STATUS;
    }

    @Override
    public Field<TransactionType> field5() {
        return Transaction.TRANSACTION.TYPE;
    }

    @Override
    public Field<Currency> field6() {
        return Transaction.TRANSACTION.CURRENCY;
    }

    @Override
    public Field<BigDecimal> field7() {
        return Transaction.TRANSACTION.VALUE;
    }

    @Override
    public Field<String> field8() {
        return Transaction.TRANSACTION.ERROR_MESSAGE;
    }

    @Override
    public Field<Long> field9() {
        return Transaction.TRANSACTION.EVENT_TIMESTAMP;
    }

    @Override
    public TransactionId component1() {
        return getTransactionId();
    }

    @Override
    public UserId component2() {
        return getTransferredFrom();
    }

    @Override
    public UserId component3() {
        return getTransferredTo();
    }

    @Override
    public TransactionStatus component4() {
        return getStatus();
    }

    @Override
    public TransactionType component5() {
        return getType();
    }

    @Override
    public Currency component6() {
        return getCurrency();
    }

    @Override
    public BigDecimal component7() {
        return getValue();
    }

    @Override
    public String component8() {
        return getErrorMessage();
    }

    @Override
    public Long component9() {
        return getEventTimestamp();
    }

    @Override
    public TransactionId value1() {
        return getTransactionId();
    }

    @Override
    public UserId value2() {
        return getTransferredFrom();
    }

    @Override
    public UserId value3() {
        return getTransferredTo();
    }

    @Override
    public TransactionStatus value4() {
        return getStatus();
    }

    @Override
    public TransactionType value5() {
        return getType();
    }

    @Override
    public Currency value6() {
        return getCurrency();
    }

    @Override
    public BigDecimal value7() {
        return getValue();
    }

    @Override
    public String value8() {
        return getErrorMessage();
    }

    @Override
    public Long value9() {
        return getEventTimestamp();
    }

    @Override
    public TransactionRecord value1(TransactionId value) {
        setTransactionId(value);
        return this;
    }

    @Override
    public TransactionRecord value2(UserId value) {
        setTransferredFrom(value);
        return this;
    }

    @Override
    public TransactionRecord value3(UserId value) {
        setTransferredTo(value);
        return this;
    }

    @Override
    public TransactionRecord value4(TransactionStatus value) {
        setStatus(value);
        return this;
    }

    @Override
    public TransactionRecord value5(TransactionType value) {
        setType(value);
        return this;
    }

    @Override
    public TransactionRecord value6(Currency value) {
        setCurrency(value);
        return this;
    }

    @Override
    public TransactionRecord value7(BigDecimal value) {
        setValue(value);
        return this;
    }

    @Override
    public TransactionRecord value8(String value) {
        setErrorMessage(value);
        return this;
    }

    @Override
    public TransactionRecord value9(Long value) {
        setEventTimestamp(value);
        return this;
    }

    @Override
    public TransactionRecord values(TransactionId value1, UserId value2, UserId value3, TransactionStatus value4, TransactionType value5, Currency value6, BigDecimal value7, String value8, Long value9) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        value7(value7);
        value8(value8);
        value9(value9);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached TransactionRecord
     */
    public TransactionRecord() {
        super(Transaction.TRANSACTION);
    }

    /**
     * Create a detached, initialised TransactionRecord
     */
    public TransactionRecord(TransactionId transactionId, UserId transferredFrom, UserId transferredTo, TransactionStatus status, TransactionType type, Currency currency, BigDecimal value, String errorMessage, Long eventTimestamp) {
        super(Transaction.TRANSACTION);

        set(0, transactionId);
        set(1, transferredFrom);
        set(2, transferredTo);
        set(3, status);
        set(4, type);
        set(5, currency);
        set(6, value);
        set(7, errorMessage);
        set(8, eventTimestamp);
    }
}
