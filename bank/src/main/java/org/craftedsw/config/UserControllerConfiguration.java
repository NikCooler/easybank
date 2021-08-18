package org.craftedsw.config;

import io.javalin.Javalin;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.controller.UserController;
import org.craftedsw.service.user.moneyaccount.create.MoneyAccountCreateCommand;
import org.craftedsw.service.user.moneyaccount.create.MoneyAccountCreateCommandProcessor;
import org.craftedsw.service.user.moneyaccount.create.MoneyAccountCreateCommandValidator;
import org.craftedsw.service.user.profile.UserProfileQuery;
import org.craftedsw.service.user.profile.UserProfileQueryProcessor;
import org.craftedsw.service.user.profile.UserProfileQueryValidator;
import org.craftedsw.service.user.register.UserRegisterCommand;
import org.craftedsw.service.user.register.UserRegisterCommandProcessor;
import org.craftedsw.service.user.register.UserRegisterCommandValidator;
import org.craftedsw.writelane.EventStoreServiceImpl;
import org.jooq.DSLContext;

import static org.craftedsw.config.AppEndpoint.*;

public final class UserControllerConfiguration extends ControllerConfiguration {

    public UserControllerConfiguration(Javalin app, EventStoreServiceImpl eventStoreService, DSLContext dslContext) {
        super(app, eventStoreService, dslContext);
    }

    public void init() {
        var userController = new UserController(
                new UserRegisterCommandProcessor(eventStoreService, new UserRegisterCommandValidator()),
                new UserProfileQueryProcessor(dslContext, new UserProfileQueryValidator(dslContext)),
                new MoneyAccountCreateCommandProcessor(eventStoreService, new MoneyAccountCreateCommandValidator())
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

        initGetRequest(GET_USER_PROFILE,
                ctx -> new UserProfileQuery(UserId.valueOf(ctx.pathParam("userId"))),
                userController::userProfile
        );
    }

}
