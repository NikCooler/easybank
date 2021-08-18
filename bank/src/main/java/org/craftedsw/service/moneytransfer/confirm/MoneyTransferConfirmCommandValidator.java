package org.craftedsw.service.moneytransfer.confirm;

import org.craftedsw.aggregate.AggregateStateBase;
import org.craftedsw.aggregate.MoneyTransferAggregateState;
import org.craftedsw.aggregate.UserAggregateState;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.cqrs.command.CommandValidator;
import org.craftedsw.type.Currency;
import org.craftedsw.type.TransferStatus;
import org.craftedsw.writelane.EventStoreService;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferConfirmCommandValidator implements CommandValidator<MoneyTransferConfirmCommand> {

    private final EventStoreService eventStoreService;

    public MoneyTransferConfirmCommandValidator(EventStoreService eventStoreService) {
        this.eventStoreService = eventStoreService;
    }

    @Override
    public boolean validate(MoneyTransferConfirmCommand command, AggregateStateBase aggregateState) {
        String prefixErrorMessage = "Money transfer confirmation [ " + command.getAggregateId() + " ]";

        if (Objects.isNull(aggregateState)) {
            throw new CommandValidationException("Money transfer request is not found by id [ " + command.getAggregateId() + " ]");
        }
        var transferState = (MoneyTransferAggregateState) aggregateState;

        if (transferState.getTransferStatus() != TransferStatus.PROCESSING) {
            throw new CommandValidationException("Money transfer request by id [ " + command.getAggregateId() + " ] has incorrect status [ " + transferState.getTransferStatus() + " ]");
        }

        var userFromState = (UserAggregateState) eventStoreService.retrieveAggregate(transferState.getTransferFrom());
        var userToState   = (UserAggregateState) eventStoreService.retrieveAggregate(transferState.getTransferTo());

        Map<Currency, BigDecimal> userFromAccounts = userFromState.getMoneyAccounts();
        if (!userFromAccounts.containsKey(transferState.getCurrency())) {
            throw new CommandValidationException(prefixErrorMessage + " failed due to 'user from' [ " + userFromState.getAggregateId() + " ] " +
                    "doesn't have money account [ '" + transferState.getCurrency() + "' ]");
        }
        BigDecimal userFromMoney = userFromAccounts.get(transferState.getCurrency());
        if (userFromMoney.compareTo(transferState.getValue()) < 0) {
            throw new CommandValidationException(prefixErrorMessage + " failed due to 'user from' [ " + userFromState.getAggregateId() + " ] " +
                    "doesn't have enough money [ '" + transferState.getCurrency() + "' ]");
        }
        if (!userToState.getMoneyAccounts().containsKey(transferState.getCurrency())) {
            throw new CommandValidationException(prefixErrorMessage + " failed due to 'user to' [ " + userToState.getAggregateId() + " ] " +
                    "doesn't have money account [ '" + transferState.getCurrency() + "' ]");
        }
        return true;
    }
}
