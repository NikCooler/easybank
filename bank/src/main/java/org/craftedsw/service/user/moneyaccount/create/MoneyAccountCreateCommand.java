package org.craftedsw.service.user.moneyaccount.create;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.command.CommandBase;
import org.craftedsw.type.Currency;

import java.math.BigDecimal;

/**
 * @author Nikolay Smirnov
 */
public class MoneyAccountCreateCommand extends CommandBase<UserId> {

    private Currency currency;
    private BigDecimal value;

    public MoneyAccountCreateCommand(UserId aggregateId) {
        super(aggregateId);
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
