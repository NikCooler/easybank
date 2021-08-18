/*
 * This file is generated by jOOQ.
 */
package org.craftedsw.model;


import java.util.Arrays;
import java.util.List;

import org.craftedsw.model.tables.EventStore;
import org.craftedsw.model.tables.MoneyAccount;
import org.craftedsw.model.tables.MoneyTransfer;
import org.craftedsw.model.tables.User;
import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class DefaultSchema extends SchemaImpl {

    private static final long serialVersionUID = -1905873089;

    /**
     * The reference instance of <code>DEFAULT_SCHEMA</code>
     */
    public static final DefaultSchema DEFAULT_SCHEMA = new DefaultSchema();

    /**
     * No further instances allowed
     */
    private DefaultSchema() {
        super("", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            EventStore.EVENT_STORE,
            MoneyAccount.MONEY_ACCOUNT,
            MoneyTransfer.MONEY_TRANSFER,
            User.USER);
    }
}
