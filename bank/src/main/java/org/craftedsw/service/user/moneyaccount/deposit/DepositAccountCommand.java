package org.craftedsw.service.user.moneyaccount.deposit;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.command.CommandBase;
import org.craftedsw.type.Currency;

import java.math.BigDecimal;

/**
 * @author Nikolay Smirnov
 */
public class DepositAccountCommand extends CommandBase<UserId> {

    private Currency currency;
    private BigDecimal value;

    public DepositAccountCommand(UserId aggregateId) {
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
