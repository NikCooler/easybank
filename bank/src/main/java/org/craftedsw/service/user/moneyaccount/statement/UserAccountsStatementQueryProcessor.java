package org.craftedsw.service.user.moneyaccount.statement;

import org.craftedsw.aggregate.UserAggregateState;
import org.craftedsw.cqrs.query.QueryProcessorBase;
import org.craftedsw.dto.UserAccountsStatementDto;
import org.craftedsw.type.StatementFilter;
import org.craftedsw.writelane.EventStoreService;
import org.jooq.DSLContext;

/**
 * @author Nikolay Smirnov
 */
public class UserAccountsStatementQueryProcessor extends QueryProcessorBase<UserAccountsStatementQuery, UserAccountsStatementDto> {

    private final EventStoreService eventStoreService;

    public UserAccountsStatementQueryProcessor(
            DSLContext readModelContext,
            UserAccountsStatementQueryValidator validator,
            EventStoreService eventStoreService
    ) {
        super(readModelContext, validator);
        this.eventStoreService = eventStoreService;
    }

    @Override
    protected UserAccountsStatementDto buildPayload(UserAccountsStatementQuery query) {
        var userState = (UserAggregateState) eventStoreService.retrieveAggregate(query.userId);
        var accountsStatement = userState.getUserAccountsStatement();

        var filter = new StatementFilter(query.getDateFrom(), query.getDateTo());
        filter.addAllTypes(query.getStatementTypes());
        var statementRows = accountsStatement.filterAndCollectStatementRows(filter);

        return new UserAccountsStatementDto(statementRows);
    }
}
