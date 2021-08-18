/*
 * This file is generated by jOOQ.
 */
package org.craftedsw.model.tables;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.TransactionId.JooqTransactionIdConverter;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.aggregate.UserId.JooqUserIdConverter;
import org.craftedsw.model.DefaultSchema;
import org.craftedsw.model.tables.records.TransactionRecord;
import org.craftedsw.type.Currency;
import org.craftedsw.type.TransactionStatus;
import org.craftedsw.type.TransactionType;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Row9;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Transaction extends TableImpl<TransactionRecord> {

    private static final long serialVersionUID = 200854435;

    /**
     * The reference instance of <code>TRANSACTION</code>
     */
    public static final Transaction TRANSACTION = new Transaction();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<TransactionRecord> getRecordType() {
        return TransactionRecord.class;
    }

    /**
     * The column <code>TRANSACTION.TRANSACTION_ID</code>.
     */
    public final TableField<TransactionRecord, TransactionId> TRANSACTION_ID = createField(DSL.name("TRANSACTION_ID"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "", new JooqTransactionIdConverter());

    /**
     * The column <code>TRANSACTION.TRANSFERRED_FROM</code>.
     */
    public final TableField<TransactionRecord, UserId> TRANSFERRED_FROM = createField(DSL.name("TRANSFERRED_FROM"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "", new JooqUserIdConverter());

    /**
     * The column <code>TRANSACTION.TRANSFERRED_TO</code>.
     */
    public final TableField<TransactionRecord, UserId> TRANSFERRED_TO = createField(DSL.name("TRANSFERRED_TO"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "", new JooqUserIdConverter());

    /**
     * The column <code>TRANSACTION.STATUS</code>.
     */
    public final TableField<TransactionRecord, TransactionStatus> STATUS = createField(DSL.name("STATUS"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false), this, "", new org.jooq.impl.EnumConverter<java.lang.String, org.craftedsw.type.TransactionStatus>(java.lang.String.class, org.craftedsw.type.TransactionStatus.class));

    /**
     * The column <code>TRANSACTION.TYPE</code>.
     */
    public final TableField<TransactionRecord, TransactionType> TYPE = createField(DSL.name("TYPE"), org.jooq.impl.SQLDataType.VARCHAR(30).nullable(false), this, "", new org.jooq.impl.EnumConverter<java.lang.String, org.craftedsw.type.TransactionType>(java.lang.String.class, org.craftedsw.type.TransactionType.class));

    /**
     * The column <code>TRANSACTION.CURRENCY</code>.
     */
    public final TableField<TransactionRecord, Currency> CURRENCY = createField(DSL.name("CURRENCY"), org.jooq.impl.SQLDataType.VARCHAR(3).nullable(false), this, "", new org.jooq.impl.EnumConverter<java.lang.String, org.craftedsw.type.Currency>(java.lang.String.class, org.craftedsw.type.Currency.class));

    /**
     * The column <code>TRANSACTION.VALUE</code>.
     */
    public final TableField<TransactionRecord, BigDecimal> VALUE = createField(DSL.name("VALUE"), org.jooq.impl.SQLDataType.DECIMAL(10, 2).nullable(false), this, "");

    /**
     * The column <code>TRANSACTION.ERROR_MESSAGE</code>.
     */
    public final TableField<TransactionRecord, String> ERROR_MESSAGE = createField(DSL.name("ERROR_MESSAGE"), org.jooq.impl.SQLDataType.VARCHAR(300), this, "");

    /**
     * The column <code>TRANSACTION.EVENT_TIMESTAMP</code>.
     */
    public final TableField<TransactionRecord, Long> EVENT_TIMESTAMP = createField(DSL.name("EVENT_TIMESTAMP"), org.jooq.impl.SQLDataType.BIGINT.nullable(false), this, "");

    /**
     * Create a <code>TRANSACTION</code> table reference
     */
    public Transaction() {
        this(DSL.name("TRANSACTION"), null);
    }

    /**
     * Create an aliased <code>TRANSACTION</code> table reference
     */
    public Transaction(String alias) {
        this(DSL.name(alias), TRANSACTION);
    }

    /**
     * Create an aliased <code>TRANSACTION</code> table reference
     */
    public Transaction(Name alias) {
        this(alias, TRANSACTION);
    }

    private Transaction(Name alias, Table<TransactionRecord> aliased) {
        this(alias, aliased, null);
    }

    private Transaction(Name alias, Table<TransactionRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<TransactionRecord> getPrimaryKey() {
        return Internal.createUniqueKey(Transaction.TRANSACTION, "PK_TRANSACTION", new TableField[] { Transaction.TRANSACTION.TRANSACTION_ID }, true);
    }

    @Override
    public List<UniqueKey<TransactionRecord>> getKeys() {
        return Arrays.<UniqueKey<TransactionRecord>>asList(
              Internal.createUniqueKey(Transaction.TRANSACTION, "PK_TRANSACTION", new TableField[] { Transaction.TRANSACTION.TRANSACTION_ID }, true)
        );
    }

    @Override
    public Transaction as(String alias) {
        return new Transaction(DSL.name(alias), this);
    }

    @Override
    public Transaction as(Name alias) {
        return new Transaction(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Transaction rename(String name) {
        return new Transaction(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Transaction rename(Name name) {
        return new Transaction(name, null);
    }

    // -------------------------------------------------------------------------
    // Row9 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row9<TransactionId, UserId, UserId, TransactionStatus, TransactionType, Currency, BigDecimal, String, Long> fieldsRow() {
        return (Row9) super.fieldsRow();
    }
}
