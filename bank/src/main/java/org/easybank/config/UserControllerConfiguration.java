package org.easybank.config;

import io.javalin.Javalin;
import org.easybank.aggregate.UserId;
import org.easybank.controller.UserController;
import org.easybank.service.user.moneyaccount.create.MoneyAccountCreateCommand;
import org.easybank.service.user.moneyaccount.create.MoneyAccountCreateCommandProcessor;
import org.easybank.service.user.moneyaccount.create.MoneyAccountCreateCommandValidator;
import org.easybank.service.user.moneyaccount.deposit.DepositAccountCommand;
import org.easybank.service.user.moneyaccount.deposit.DepositAccountCommandProcessor;
import org.easybank.service.user.moneyaccount.deposit.DepositAccountCommandValidator;
import org.easybank.service.user.moneyaccount.statement.UserAccountsStatementQuery;
import org.easybank.service.user.moneyaccount.statement.UserAccountsStatementQueryProcessor;
import org.easybank.service.user.moneyaccount.statement.UserAccountsStatementQueryValidator;
import org.easybank.service.user.moneyaccount.withdraw.WithdrawAccountCommand;
import org.easybank.service.user.moneyaccount.withdraw.WithdrawAccountCommandProcessor;
import org.easybank.service.user.moneyaccount.withdraw.WithdrawAccountCommandValidator;
import org.easybank.service.user.profile.UserProfileQuery;
import org.easybank.service.user.profile.UserProfileQueryProcessor;
import org.easybank.service.user.profile.UserProfileQueryValidator;
import org.easybank.service.user.register.UserRegisterCommand;
import org.easybank.service.user.register.UserRegisterCommandProcessor;
import org.easybank.service.user.register.UserRegisterCommandValidator;
import org.easybank.writelane.EventStoreServiceImpl;
import org.jooq.DSLContext;

import static org.easybank.config.AppEndpoint.*;

public final class UserControllerConfiguration extends ControllerConfiguration {

    public UserControllerConfiguration(Javalin app, EventStoreServiceImpl eventStoreService, DSLContext dslContext) {
        super(app, eventStoreService, dslContext);
    }

    public void init() {
        var userController = new UserController(
                new UserRegisterCommandProcessor(eventStoreService, new UserRegisterCommandValidator()),
                new UserProfileQueryProcessor(dslContext, new UserProfileQueryValidator(dslContext)),
                new MoneyAccountCreateCommandProcessor(eventStoreService, new MoneyAccountCreateCommandValidator()),
                new DepositAccountCommandProcessor(eventStoreService, new DepositAccountCommandValidator()),
                new WithdrawAccountCommandProcessor(eventStoreService, new WithdrawAccountCommandValidator()),
                new UserAccountsStatementQueryProcessor(dslContext, new UserAccountsStatementQueryValidator(dslContext), eventStoreService)
        );

        initPostRequest(
                POST_USER_REGISTER,
                ctx -> new UserRegisterCommand(UserId.valueOf(ctx.pathParam("userId"))),
                userController::register
        );

        initPutRequest(
                PUT_USER_MONEY_ACCOUNT,
                ctx -> new MoneyAccountCreateCommand(UserId.valueOf(ctx.pathParam("userId"))),
                userController::createMoneyAccount
        );

        initPutRequest(
                PUT_USER_DEPOSIT_ACCOUNT,
                ctx -> new DepositAccountCommand(UserId.valueOf(ctx.pathParam("userId"))),
                userController::depositAccount
        );

        initPutRequest(
                PUT_USER_WITHDRAW_ACCOUNT,
                ctx -> new WithdrawAccountCommand(UserId.valueOf(ctx.pathParam("userId"))),
                userController::withdrawAccount
        );

        initGetRequest(GET_USER_PROFILE,
                ctx -> new UserProfileQuery(UserId.valueOf(ctx.pathParam("userId"))),
                userController::userProfile
        );

        initGetRequest(GET_USER_ACCOUNTS_STATEMENT,
                ctx -> new UserAccountsStatementQuery(
                            UserId.valueOf(ctx.pathParam("userId")),
                            ctx.queryParams("statementTypes"),
                            ctx.queryParam("dateFrom"),
                            ctx.queryParam("dateTo")
                ),
                userController::userAccountsStatement
        );
    }

}
