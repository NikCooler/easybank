package org.craftedsw.aggregate;

import org.craftedsw.event.MoneyAccountCreatedEvent;
import org.craftedsw.event.DepositEvent;
import org.craftedsw.event.WithdrawnEvent;
import org.craftedsw.event.UserRegisteredEvent;
import org.craftedsw.type.*;
import org.craftedsw.type.Currency;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

import static java.util.Collections.unmodifiableMap;

/**
 * @author Nikolay Smirnov
 */
public class UserAggregateState extends AggregateStateBase<UserId> {

    private String email;

    private final Map<Currency, MoneyAccount> accounts = new HashMap<>();
    private final UserAccountsStatement userAccountsStatement = new UserAccountsStatement();

    UserAggregateState(UserId aggregateId) {
        super(aggregateId);
    }

    public void apply(UserRegisteredEvent event) {
        this.email = event.getEmail();
    }

    public void apply(MoneyAccountCreatedEvent event) {
        var moneyAccount = event.getMoneyAccount();
        this.accounts.put(moneyAccount.getCurrency(), moneyAccount);
    }

    public void apply(WithdrawnEvent event) {
        var withdrawAmount = event.getAmount();
        var account = accounts.get(withdrawAmount.getCurrency());
        var total = account.withdraw(withdrawAmount);

        userAccountsStatement.addTransactionStatement(buildTransactionStatement(event.getAmount(), total, StatementType.DEBIT, event.getEventTimestamp()));
    }

    public void apply(DepositEvent event) {
        var depositAmount = event.getAmount();
        var account = accounts.get(depositAmount.getCurrency());
        var total = account.deposit(depositAmount);

        userAccountsStatement.addTransactionStatement(buildTransactionStatement(event.getAmount(), total, StatementType.CREDIT, event.getEventTimestamp()));
    }

    public String getEmail() {
        return email;
    }

    public Map<Currency, MoneyAccount> getAccounts() {
        return unmodifiableMap(accounts);
    }

    public List<String> getUserAccountsStatementInfo() {
        return userAccountsStatement.buildStatements();
    }

    private TransactionStatement buildTransactionStatement(Amount change, Amount total, StatementType type, Long timestamp) {
        var act = new StatementAct(change, total);
        var date = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(timestamp),
                TimeZone.getDefault().toZoneId()
        );

        return new TransactionStatement(type, act, date);
    }
}
