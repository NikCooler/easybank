package org.craftedsw.controller;

import org.craftedsw.cqrs.Response;
import org.craftedsw.service.user.moneyaccount.create.MoneyAccountCreateCommand;
import org.craftedsw.service.user.moneyaccount.create.MoneyAccountCreateCommandProcessor;
import org.craftedsw.service.user.moneyaccount.deposit.DepositAccountCommand;
import org.craftedsw.service.user.moneyaccount.deposit.DepositAccountCommandProcessor;
import org.craftedsw.service.user.moneyaccount.statement.UserAccountsStatementQuery;
import org.craftedsw.service.user.moneyaccount.statement.UserAccountsStatementQueryProcessor;
import org.craftedsw.service.user.moneyaccount.withdraw.WithdrawAccountCommand;
import org.craftedsw.service.user.moneyaccount.withdraw.WithdrawAccountCommandProcessor;
import org.craftedsw.service.user.profile.UserProfileQuery;
import org.craftedsw.service.user.profile.UserProfileQueryProcessor;
import org.craftedsw.service.user.register.UserRegisterCommand;
import org.craftedsw.service.user.register.UserRegisterCommandProcessor;

/**
 * @author Nikolay Smirnov
 */
public class UserController {

    private final UserRegisterCommandProcessor registerCommandProcessor;
    private final UserProfileQueryProcessor userProfileQueryProcessor;
    private final DepositAccountCommandProcessor depositAccountCommandProcessor;
    private final WithdrawAccountCommandProcessor withdrawAccountCommandProcessor;
    private final UserAccountsStatementQueryProcessor userAccountsStatementQueryProcessor;

    private final MoneyAccountCreateCommandProcessor moneyAccountCreateCommandProcessor;

    public UserController(
            UserRegisterCommandProcessor registerCommandProcessor,
            UserProfileQueryProcessor userProfileQueryProcessor,
            MoneyAccountCreateCommandProcessor moneyAccountCreateCommandProcessor,
            DepositAccountCommandProcessor depositAccountCommandProcessor,
            WithdrawAccountCommandProcessor withdrawAccountCommandProcessor,
            UserAccountsStatementQueryProcessor userAccountsStatementQueryProcessor
    ) {
        this.registerCommandProcessor = registerCommandProcessor;
        this.userProfileQueryProcessor = userProfileQueryProcessor;
        this.moneyAccountCreateCommandProcessor = moneyAccountCreateCommandProcessor;
        this.depositAccountCommandProcessor = depositAccountCommandProcessor;
        this.withdrawAccountCommandProcessor = withdrawAccountCommandProcessor;
        this.userAccountsStatementQueryProcessor = userAccountsStatementQueryProcessor;
    }

    // POST
    public Response register(UserRegisterCommand command) {
        return registerCommandProcessor.executeCommand(command);
    }

    // PUT
    public Response createMoneyAccount(MoneyAccountCreateCommand command) {
        return moneyAccountCreateCommandProcessor.executeCommand(command);
    }

    // PUT
    public Response depositAccount(DepositAccountCommand command) {
        return depositAccountCommandProcessor.executeCommand(command);
    }

    // PUT
    public Response withdrawAccount(WithdrawAccountCommand command) {
        return withdrawAccountCommandProcessor.executeCommand(command);
    }

    // GET
    public Response userAccountsStatement(UserAccountsStatementQuery query) {
        return userAccountsStatementQueryProcessor.executeQuery(query);
    }

    // GET
    public Response userProfile(UserProfileQuery query) {
        return userProfileQueryProcessor.executeQuery(query);
    }

}
