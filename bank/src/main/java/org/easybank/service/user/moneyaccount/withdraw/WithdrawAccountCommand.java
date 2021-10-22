package org.easybank.service.user.moneyaccount.withdraw;

import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandBase;
import org.easybank.type.Currency;

import java.math.BigDecimal;

/**
 * @author Nikolay Smirnov
 */
public class WithdrawAccountCommand extends CommandBase<UserId> {

    private Currency currency;
    private BigDecimal value;

    public WithdrawAccountCommand(UserId aggregateId) {
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
