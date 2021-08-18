package org.craftedsw.aggregate;

import org.craftedsw.event.MoneyAccountCreatedEvent;
import org.craftedsw.event.MoneyToppedUpEvent;
import org.craftedsw.event.MoneyWithdrawnEvent;
import org.craftedsw.event.UserRegisteredEvent;
import org.craftedsw.type.Currency;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * @author Nikolay Smirnov
 */
public class UserAggregateState extends AggregateStateBase<UserId> {

    private String email;

    private final Map<Currency, BigDecimal> moneyAccounts = new HashMap<>();

    UserAggregateState(UserId aggregateId) {
        super(aggregateId);
    }

    public void apply(UserRegisteredEvent event) {
        this.email = event.getEmail();
    }

    public void apply(MoneyAccountCreatedEvent event) {
        moneyAccounts.put(
                event.getMoneyAccount().getCurrency(),
                event.getMoneyAccount().getValue()
        );
    }

    public void apply(MoneyWithdrawnEvent event) {
        BigDecimal val = moneyAccounts.get(event.getCurrency());
        moneyAccounts.put(event.getCurrency(), val.min(event.getValue()));
    }

    public void apply(MoneyToppedUpEvent event) {
        BigDecimal val = moneyAccounts.get(event.getCurrency());
        moneyAccounts.put(event.getCurrency(), val.add(event.getValue()));
    }

    //
    // Getters and setters
    //


    public String getEmail() {
        return email;
    }

    public Map<Currency, BigDecimal> getMoneyAccounts() {
        return unmodifiableMap(moneyAccounts);
    }
}
