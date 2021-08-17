package org.craftedsw;

import io.javalin.Javalin;

/**
 * @author Nikolay Smirnov
 */
public final class BankApp {

    private Javalin app;

    private BankApp() {}

    public void run() {
        if (app != null) {
            return;
        }

        app = Javalin.create();
        app.start(8080);
    }

    public static BankApp getInstance() {
        return LazyBankAppHolder.APP;
    }

    private static class LazyBankAppHolder {
        private static final BankApp APP = new BankApp();
    }

}