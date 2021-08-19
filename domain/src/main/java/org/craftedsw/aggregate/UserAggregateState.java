package org.craftedsw.aggregate;

import org.craftedsw.event.MoneyAccountCreatedEvent;
import org.craftedsw.event.DepositEvent;
import org.craftedsw.event.WithdrawnEvent;
import org.craftedsw.event.UserRegisteredEvent;
import org.craftedsw.type.*;
import org.craftedsw.type.Currency;

import java.util.*;

import static java.util.Collections.unmodifiableMap;
import static org.craftedsw.util.DateUtil.toLocalDateTime;

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
        var total = Amount.copyOf(account.withdraw(withdrawAmount));

        userAccountsStatement.addTransactionStatement(buildTransactionStatement(event.getAmount(), total, StatementType.DEBIT, event.getEventTimestamp()));
    }

    public void apply(DepositEvent event) {
        var depositAmount = event.getAmount();
        var account = accounts.get(depositAmount.getCurrency());
        var total = Amount.copyOf(account.deposit(depositAmount));

        userAccountsStatement.addTransactionStatement(buildTransactionStatement(event.getAmount(), total, StatementType.CREDIT, event.getEventTimestamp()));
    }

    public String getEmail() {
        return email;
    }

    public Map<Currency, MoneyAccount> getAccounts() {
        return unmodifiableMap(accounts);
    }

    public UserAccountsStatement getUserAccountsStatement() {
        return userAccountsStatement;
    }

    private TransactionStatement buildTransactionStatement(Amount change, Amount total, StatementType type, Long timestamp) {
        var act = new StatementAct(change, total);
        var date = toLocalDateTime(timestamp);

        return new TransactionStatement(type, act, date);
    }
}
