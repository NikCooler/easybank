package org.craftedsw.service.moneytransfer;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserAggregateState;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.type.Amount;
import org.craftedsw.type.Currency;
import org.craftedsw.type.MoneyAccount;

import java.util.Map;

import static java.lang.String.format;

/**
 * @author Nikolay Smirnov
 */
public final class MoneyTransferValidator {
    private static final String USER_SENDER_TITLE = "Sender";
    private static final String USER_RECEIVER_TITLE = "Receiver";

    public void validateTransfer(TransactionId trxId, UserAggregateState sender, UserAggregateState receiver, Amount transferAmount) {
        String prefixErrorMessage = format("Money transfer [ %s ] failed due to ", trxId);

        validateUserHasAccount(sender, transferAmount, prefixErrorMessage, USER_SENDER_TITLE);
        validateUserHasEnoughMoney(sender, transferAmount, prefixErrorMessage, USER_SENDER_TITLE);
        validateUserHasAccount(receiver, transferAmount, prefixErrorMessage, USER_RECEIVER_TITLE);
    }

    public void validateUserHasAccount(UserAggregateState userState, Amount transferAmount, String prefixErrorMessage, String userTitle) {
        Map<Currency, MoneyAccount> accounts = userState.getAccounts();
        if (!accounts.containsKey(transferAmount.getCurrency())) {
            throw new CommandValidationException(prefixErrorMessage + format("'%s' [ %s ] doesn't have money account [ %s ]",
                    userTitle, userState.getAggregateId(),transferAmount.getCurrency()));
        }
    }

    public void validateUserHasEnoughMoney(UserAggregateState userState, Amount transferAmount, String prefixErrorMessage, String userTitle) {
        Map<Currency, MoneyAccount> accounts = userState.getAccounts();
        MoneyAccount account = accounts.get(transferAmount.getCurrency());
        if (account.compareValueTo(transferAmount) < 0) {
            throw new CommandValidationException(prefixErrorMessage + format("'%s' [ %s ] doesn't have enough money [ %s ]",
                    userTitle, userState.getAggregateId(), transferAmount.getCurrency()));
        }
    }
}
