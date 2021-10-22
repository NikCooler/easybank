/*
 * This file is generated by jOOQ.
 */
package org.easybank.model.tables;


import java.math.BigDecimal;

import org.easybank.aggregate.UserId;
import org.easybank.aggregate.UserId.JooqUserIdConverter;
import org.easybank.model.DefaultSchema;
import org.easybank.model.tables.records.MoneyAccountRecord;
import org.easybank.type.Currency;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MoneyAccount extends TableImpl<MoneyAccountRecord> {

    private static final long serialVersionUID = -948943677;

    /**
     * The reference instance of <code>MONEY_ACCOUNT</code>
     */
    public static final MoneyAccount MONEY_ACCOUNT = new MoneyAccount();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<MoneyAccountRecord> getRecordType() {
        return MoneyAccountRecord.class;
    }

    /**
     * The column <code>MONEY_ACCOUNT.USER_ID</code>.
     */
    public final TableField<MoneyAccountRecord, UserId> USER_ID = createField(DSL.name("USER_ID"), org.jooq.impl.SQLDataType.VARCHAR(36).nullable(false), this, "", new JooqUserIdConverter());

    /**
     * The column <code>MONEY_ACCOUNT.CURRENCY</code>.
     */
    public final TableField<MoneyAccountRecord, Currency> CURRENCY = createField(DSL.name("CURRENCY"), org.jooq.impl.SQLDataType.VARCHAR(3).nullable(false), this, "", new org.jooq.impl.EnumConverter<java.lang.String, org.easybank.type.Currency>(java.lang.String.class, org.easybank.type.Currency.class));

    /**
     * The column <code>MONEY_ACCOUNT.VALUE</code>.
     */
    public final TableField<MoneyAccountRecord, BigDecimal> VALUE = createField(DSL.name("VALUE"), org.jooq.impl.SQLDataType.DECIMAL(10, 2).nullable(false), this, "");

    /**
     * Create a <code>MONEY_ACCOUNT</code> table reference
     */
    public MoneyAccount() {
        this(DSL.name("MONEY_ACCOUNT"), null);
    }

    /**
     * Create an aliased <code>MONEY_ACCOUNT</code> table reference
     */
    public MoneyAccount(String alias) {
        this(DSL.name(alias), MONEY_ACCOUNT);
    }

    /**
     * Create an aliased <code>MONEY_ACCOUNT</code> table reference
     */
    public MoneyAccount(Name alias) {
        this(alias, MONEY_ACCOUNT);
    }

    private MoneyAccount(Name alias, Table<MoneyAccountRecord> aliased) {
        this(alias, aliased, null);
    }

    private MoneyAccount(Name alias, Table<MoneyAccountRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public MoneyAccount as(String alias) {
        return new MoneyAccount(DSL.name(alias), this);
    }

    @Override
    public MoneyAccount as(Name alias) {
        return new MoneyAccount(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public MoneyAccount rename(String name) {
        return new MoneyAccount(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public MoneyAccount rename(Name name) {
        return new MoneyAccount(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<UserId, Currency, BigDecimal> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}
