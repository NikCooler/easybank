package org.easybank.service.user.moneyaccount.statement;

import org.easybank.cqrs.query.QueryValidationException;
import org.easybank.cqrs.query.QueryValidator;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import static java.lang.String.format;
import static org.easybank.model.tables.User.USER;

/**
 * @author Nikolay Smirnov
 */
public class UserAccountsStatementQueryValidator implements QueryValidator<UserAccountsStatementQuery> {

    private final DSLContext readModelContext;

    public UserAccountsStatementQueryValidator(DSLContext readModelContext) {
        this.readModelContext = readModelContext;
    }

    @Override
    public boolean isValid(UserAccountsStatementQuery query) {
        boolean userExists = readModelContext.fetchExists(
                DSL.select(DSL.one())
                        .from(USER)
                        .where(USER.USER_ID.eq(query.userId)));
        if (!userExists) {
            throw new QueryValidationException(format("User doesn't exist by id [ %s ]", query.userId));
        }
        return true;
    }
}
