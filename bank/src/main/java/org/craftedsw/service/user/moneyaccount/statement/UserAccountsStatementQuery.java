package org.craftedsw.service.user.moneyaccount.statement;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.query.Query;

/**
 * @author Nikolay Smirnov
 */
public class UserAccountsStatementQuery implements Query {

    public final UserId userId;

    public UserAccountsStatementQuery(UserId userId) {
        this.userId = userId;
    }
}
