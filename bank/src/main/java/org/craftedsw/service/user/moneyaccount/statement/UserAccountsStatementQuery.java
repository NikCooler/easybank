package org.craftedsw.service.user.moneyaccount.statement;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.query.Query;
import org.craftedsw.type.StatementType;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Nikolay Smirnov
 */
public class UserAccountsStatementQuery implements Query {

    public final UserId userId;
    private Long dateFrom;
    private Long dateTo;
    private Set<StatementType> statementTypes;

    public UserAccountsStatementQuery(UserId userId, List<String> statementTypes, String dateFrom, String dateTo) {
        this.userId = userId;
        if (Objects.nonNull(statementTypes)) {
            this.statementTypes = statementTypes.stream()
                    .filter(StatementType::exists)
                    .map(StatementType::valueOf)
                    .collect(Collectors.toSet());
        }
        if (Objects.nonNull(dateFrom)) {
            this.dateFrom = Long.valueOf(dateFrom);
        }
        if (Objects.nonNull(dateTo)) {
            this.dateTo = Long.valueOf(dateTo);
        }
    }

    public Long getDateFrom() {
        return dateFrom;
    }

    public Long getDateTo() {
        return dateTo;
    }

    public Set<StatementType> getStatementTypes() {
        return statementTypes;
    }
}
