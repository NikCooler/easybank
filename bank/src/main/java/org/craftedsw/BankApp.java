package org.craftedsw;

import io.javalin.Javalin;
import io.javalin.plugin.json.JavalinJackson;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.config.AppConfiguration;
import org.craftedsw.config.DBConfig;
import org.craftedsw.controller.UserController;
import org.craftedsw.readlane.ReadModelUpdater;
import org.craftedsw.service.user.profile.UserProfileQuery;
import org.craftedsw.service.user.profile.UserProfileQueryProcessor;
import org.craftedsw.service.user.profile.UserProfileQueryValidator;
import org.craftedsw.service.user.register.UserRegisterCommand;
import org.craftedsw.service.user.register.UserRegisterCommandProcessor;
import org.craftedsw.service.user.register.UserRegisterCommandValidator;
import org.craftedsw.writelane.EventStoreServiceImpl;
import org.craftedsw.writelane.eventbus.EventSavedIdDispatcher;
import org.craftedsw.writelane.eventbus.EventSavedIdProducer;
import org.craftedsw.writelane.repository.EventStoreRepositoryImpl;
import org.jooq.DSLContext;

import java.util.concurrent.LinkedBlockingQueue;

import static org.craftedsw.config.AppEndpoint.*;

/**
 * Bank app
 * - Init DB
 * - Execute <code>liquibase changeSet</code>
 * - Setup REST api calls
 * - Start event bus ( async process to transfer events from the Event Store to the Read Model )
 *
 * @author Nikolay Smirnov
 */
public final class BankApp {

    private static final String SERVER_PORT_PROPERTY = "server.port";
    private Javalin app;

    private BankApp() {}

    public void run() {
        if (app != null) {
            return;
        }

        app = Javalin.create();
        var appCnf = AppConfiguration.getInstance();
        var dslContext = DBConfig.getInstance().initDBContext();
        var producer = initEventSavedIdEventBus(dslContext);

        var eventStoreService = new EventStoreServiceImpl(new EventStoreRepositoryImpl(), dslContext, producer);

        var userController = new UserController(
                new UserRegisterCommandProcessor(eventStoreService, new UserRegisterCommandValidator()),
                new UserProfileQueryProcessor(dslContext, new UserProfileQueryValidator(dslContext))
        );

        app.post(POST_USER_REGISTER,
                ctx -> {
                    var command = new UserRegisterCommand(UserId.valueOf(ctx.pathParam("userId")));
                    command = JavalinJackson.getObjectMapper().readerForUpdating(command).readValue(ctx.body());
                    ctx.json(userController.register(command));
                }
        );

        app.get(GET_USER_PROFILE,
                ctx -> ctx.json(userController.userProfile(new UserProfileQuery(ctx.pathParam("userId"))))
        );

        app.start(appCnf.getIntProperty(SERVER_PORT_PROPERTY));
    }

    private static EventSavedIdProducer initEventSavedIdEventBus(DSLContext readModelContext) {
        var savedEventIds = new LinkedBlockingQueue<Long>();

        var producer   = new EventSavedIdProducer(savedEventIds);
        var dispatcher = new EventSavedIdDispatcher(savedEventIds);

        dispatcher.registerSubscriber(new ReadModelUpdater(readModelContext));
        new Thread(dispatcher).start();

        return producer;
    }

    public static BankApp getInstance() {
        return BankApp.LazyBankAppHolder.APP;
    }

    private static class LazyBankAppHolder {
        private static final BankApp APP = new BankApp();
    }

}