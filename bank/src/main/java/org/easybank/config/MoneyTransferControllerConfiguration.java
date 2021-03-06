package org.easybank.config;

import io.javalin.Javalin;
import org.easybank.aggregate.TransactionId;
import org.easybank.controller.MoneyTransferController;
import org.easybank.service.moneytransfer.MoneyTransferValidator;
import org.easybank.service.moneytransfer.confirm.MoneyTransferConfirmCommand;
import org.easybank.service.moneytransfer.confirm.MoneyTransferConfirmCommandProcessor;
import org.easybank.service.moneytransfer.confirm.MoneyTransferConfirmCommandValidator;
import org.easybank.service.moneytransfer.details.MoneyTransferDetailsQuery;
import org.easybank.service.moneytransfer.details.MoneyTransferDetailsQueryProcessor;
import org.easybank.service.moneytransfer.details.MoneyTransferDetailsQueryValidator;
import org.easybank.service.moneytransfer.request.MoneyTransferRequestCommand;
import org.easybank.service.moneytransfer.request.MoneyTransferRequestCommandProcessor;
import org.easybank.service.moneytransfer.request.MoneyTransferRequestCommandValidator;
import org.easybank.writelane.EventStoreServiceImpl;
import org.jooq.DSLContext;

import static org.easybank.config.AppEndpoint.*;

public final class MoneyTransferControllerConfiguration extends ControllerConfiguration {

    public MoneyTransferControllerConfiguration(Javalin app, EventStoreServiceImpl eventStoreService, DSLContext dslContext) {
        super(app, eventStoreService, dslContext);
    }

    public void init() {
        var commonTransferValidator = new MoneyTransferValidator();
        var requestValidator = new MoneyTransferRequestCommandValidator(eventStoreService, commonTransferValidator);
        var confirmValidator = new MoneyTransferConfirmCommandValidator(eventStoreService, commonTransferValidator);
        var moneyTransferController = new MoneyTransferController(
                new MoneyTransferRequestCommandProcessor(eventStoreService, requestValidator),
                new MoneyTransferConfirmCommandProcessor(eventStoreService, confirmValidator),
                new MoneyTransferDetailsQueryProcessor(dslContext, new MoneyTransferDetailsQueryValidator(dslContext))
        );

        initPostRequest(POST_MONEY_TRANSFER_REQUEST,
                ctx -> new MoneyTransferRequestCommand(TransactionId.valueOf(ctx.pathParam("transactionId"))),
                moneyTransferController::moneyTransferRequest
        );

        initPutRequest(PUT_MONEY_TRANSFER_CONFIRM,
                ctx -> new MoneyTransferConfirmCommand(TransactionId.valueOf(ctx.pathParam("transactionId"))),
                moneyTransferController::moneyTransferConfirm
        );

        initGetRequest(GET_MONEY_TRANSFER_DETAILS,
                ctx -> new MoneyTransferDetailsQuery(TransactionId.valueOf(ctx.pathParam("transactionId"))),
                moneyTransferController::moneyTransferDetails
        );
    }

}
