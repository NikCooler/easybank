package org.easybank.service.moneytransfer.request;

import org.easybank.aggregate.TransactionId;
import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandBase;
import org.easybank.type.Currency;

import java.math.BigDecimal;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferRequestCommand extends CommandBase<TransactionId> {

    private UserId transferFrom;
    private UserId transferTo;
    private Currency currency;
    private BigDecimal value;

    public MoneyTransferRequestCommand(TransactionId transactionId) {
        super(transactionId);
    }

    public UserId getTransferFrom() {
        return transferFrom;
    }

    public void setTransferFrom(UserId transferFrom) {
        this.transferFrom = transferFrom;
    }

    public UserId getTransferTo() {
        return transferTo;
    }

    public void setTransferTo(UserId transferTo) {
        this.transferTo = transferTo;
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
