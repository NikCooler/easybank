package org.easybank.service.user.profile;

import org.easybank.cqrs.query.QueryProcessorBase;
import org.easybank.dto.MoneyAccountDto;
import org.easybank.dto.UserDto;
import org.easybank.model.tables.records.UserRecord;
import org.jooq.DSLContext;

import java.util.List;

import static org.easybank.model.tables.MoneyAccount.MONEY_ACCOUNT;
import static org.easybank.model.tables.User.USER;
import static java.util.stream.Collectors.toList;

/**
 * @author Nikolay Smirnov
 */
public class UserProfileQueryProcessor extends QueryProcessorBase<UserProfileQuery, UserDto> {

    public UserProfileQueryProcessor(DSLContext readModelContext, UserProfileQueryValidator validator) {
        super(readModelContext, validator);
    }

    @Override
    protected UserDto buildPayload(UserProfileQuery query) {
        UserRecord userRec = readModelContext.selectFrom(USER)
                .where(USER.USER_ID.eq(query.userId))
                .fetchOne();

        List<MoneyAccountDto> moneyAccounts = readModelContext.selectFrom(MONEY_ACCOUNT)
                .where(MONEY_ACCOUNT.USER_ID.eq(query.userId))
                .stream()
                .map(rec -> new MoneyAccountDto(
                        rec.get(MONEY_ACCOUNT.CURRENCY),
                        rec.get(MONEY_ACCOUNT.VALUE)
                ))
                .collect(toList());

        return new UserDto(userRec.get(USER.USER_ID), userRec.get(USER.EMAIL), moneyAccounts);
    }
}
