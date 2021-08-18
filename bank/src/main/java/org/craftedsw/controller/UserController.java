package org.craftedsw.controller;

import org.craftedsw.cqrs.Response;
import org.craftedsw.service.user.moneyaccount.create.MoneyAccountCreateCommand;
import org.craftedsw.service.user.moneyaccount.create.MoneyAccountCreateCommandProcessor;
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

    private final MoneyAccountCreateCommandProcessor moneyAccountCreateCommandProcessor;

    public UserController(
            UserRegisterCommandProcessor registerCommandProcessor,
            UserProfileQueryProcessor userProfileQueryProcessor,
            MoneyAccountCreateCommandProcessor moneyAccountCreateCommandProcessor
    ) {
        this.registerCommandProcessor = registerCommandProcessor;
        this.userProfileQueryProcessor = userProfileQueryProcessor;
        this.moneyAccountCreateCommandProcessor = moneyAccountCreateCommandProcessor;
    }

    // POST
    public Response register(UserRegisterCommand command) {
        return registerCommandProcessor.executeCommand(command);
    }

    // PUT
    public Response createMoneyAccount(MoneyAccountCreateCommand command) {
        return moneyAccountCreateCommandProcessor.executeCommand(command);
    }

    // GET
    public Response userProfile(UserProfileQuery query) {
        return userProfileQueryProcessor.executeQuery(query);
    }

}
