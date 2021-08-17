package org.craftedsw;

import io.javalin.Javalin;
import org.craftedsw.config.AppConfiguration;

/**
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
        var appCnf = AppConfiguration.getInstance();
        app = Javalin.create();
        app.start(appCnf.getIntProperty(SERVER_PORT_PROPERTY));
    }

    public static BankApp getInstance() {
        return LazyBankAppHolder.APP;
    }

    private static class LazyBankAppHolder {
        private static final BankApp APP = new BankApp();
    }

}