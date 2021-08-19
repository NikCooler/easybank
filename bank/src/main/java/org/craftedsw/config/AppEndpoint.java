package org.craftedsw.config;

/**
 * @author Nikolay Smirnov
 */
public final class AppEndpoint {

    private AppEndpoint() {}

    public static final String POST_USER_REGISTER = "/user/:userId/register";
    public static final String GET_USER_PROFILE = "/user/:userId/profile";
    public static final String PUT_USER_MONEY_ACCOUNT = "/user/:userId/account/create";
    public static final String PUT_USER_DEPOSIT_ACCOUNT = "/user/:userId/account/deposit";
    public static final String PUT_USER_WITHDRAW_ACCOUNT = "/user/:userId/account/withdraw";

    public static final String POST_MONEY_TRANSFER_REQUEST = "/transfer/:transactionId/request";
    public static final String PUT_MONEY_TRANSFER_CONFIRM = "/transfer/:transactionId/confirm";
    public static final String GET_MONEY_TRANSFER_DETAILS = "/transfer/:transactionId/details";

}
