package org.craftedsw.aggregate;

import org.craftedsw.event.MoneyAccountCreatedEvent;
import org.craftedsw.event.DepositEvent;
import org.craftedsw.event.WithdrawnEvent;
import org.craftedsw.event.UserRegisteredEvent;
import org.craftedsw.type.Currency;
import org.craftedsw.type.MoneyAccount;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * @author Nikolay Smirnov
 */
public class UserAggregateState extends AggregateStateBase<UserId> {

    private String email;

    private final Map<Currency, MoneyAccount> accounts = new HashMap<>();

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
        account.withdraw(withdrawAmount);
    }

    public void apply(DepositEvent event) {
        var depositAmount = event.getAmount();
        var account = accounts.get(depositAmount.getCurrency());
        account.deposit(depositAmount);
    }

    //
    // Getters and setters
    //


    public String getEmail() {
        return email;
    }

    public Map<Currency, MoneyAccount> getAccounts() {
        return unmodifiableMap(accounts);
    }
}
