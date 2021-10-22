package org.easybank.aggregate;

import org.easybank.event.MoneyAccountCreatedEvent;
import org.easybank.event.DepositEvent;
import org.easybank.event.WithdrawnEvent;
import org.easybank.event.UserRegisteredEvent;
import org.easybank.type.*;
import org.easybank.type.Currency;

import java.util.*;

import static java.util.Collections.unmodifiableMap;
import static org.easybank.util.DateUtil.toLocalDateTime;

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
