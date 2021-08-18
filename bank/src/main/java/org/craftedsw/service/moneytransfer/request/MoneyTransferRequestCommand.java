package org.craftedsw.service.moneytransfer.request;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.command.CommandBase;
import org.craftedsw.type.Currency;

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
