package org.craftedsw;

import io.javalin.Javalin;
import org.craftedsw.config.*;
import org.craftedsw.readlane.ReadModelUpdater;
import org.craftedsw.writelane.EventStoreServiceImpl;
import org.craftedsw.writelane.eventbus.EventSavedIdDispatcher;
import org.craftedsw.writelane.eventbus.EventSavedIdProducer;
import org.craftedsw.writelane.repository.EventStoreRepositoryImpl;
import org.jooq.DSLContext;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

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
        var producer = initEventBus(dslContext);
        var eventStoreService = new EventStoreServiceImpl(new EventStoreRepositoryImpl(), dslContext, producer);
        initControllers(eventStoreService, dslContext);

        app.start(appCnf.getIntProperty(SERVER_PORT_PROPERTY));
    }

    private void initControllers(EventStoreServiceImpl eventStoreService, DSLContext dslContext) {
        List.of(
                new UserControllerConfiguration(app, eventStoreService, dslContext),
                new MoneyTransferControllerConfiguration(app, eventStoreService, dslContext)
        ).forEach(ControllerConfiguration::init);
    }

    private static EventSavedIdProducer initEventBus(DSLContext readModelContext) {
        var savedEventIds = new LinkedBlockingQueue<Long>();

        var producer = new EventSavedIdProducer(savedEventIds);
        var dispatcher = new EventSavedIdDispatcher(savedEventIds);
        runDispatcher(dispatcher, readModelContext);

        return producer;
    }

    private static void runDispatcher(EventSavedIdDispatcher dispatcher, DSLContext readModelContext) {
        dispatcher.registerSubscriber(new ReadModelUpdater(readModelContext));
        var dispatcherThread = new Thread(dispatcher);
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
    }

    public static BankApp getInstance() {
        return BankApp.LazyBankAppHolder.APP;
    }

    private static class LazyBankAppHolder {
        private static final BankApp APP = new BankApp();
    }

}