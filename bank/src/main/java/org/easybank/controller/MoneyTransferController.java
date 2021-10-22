package org.easybank.controller;

import org.easybank.cqrs.Response;
import org.easybank.service.moneytransfer.confirm.MoneyTransferConfirmCommand;
import org.easybank.service.moneytransfer.confirm.MoneyTransferConfirmCommandProcessor;
import org.easybank.service.moneytransfer.details.MoneyTransferDetailsQuery;
import org.easybank.service.moneytransfer.details.MoneyTransferDetailsQueryProcessor;
import org.easybank.service.moneytransfer.request.MoneyTransferRequestCommand;
import org.easybank.service.moneytransfer.request.MoneyTransferRequestCommandProcessor;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferController {

    private final MoneyTransferRequestCommandProcessor requestCommandProcessor;
    private final MoneyTransferConfirmCommandProcessor confirmCommandProcessor;
    private final MoneyTransferDetailsQueryProcessor detailsQueryProcessor;

    public MoneyTransferController(
            MoneyTransferRequestCommandProcessor requestCommandProcessor,
            MoneyTransferConfirmCommandProcessor confirmCommandProcessor,
            MoneyTransferDetailsQueryProcessor detailsQueryProcessor
    ) {
        this.requestCommandProcessor = requestCommandProcessor;
        this.confirmCommandProcessor = confirmCommandProcessor;
        this.detailsQueryProcessor = detailsQueryProcessor;
    }

    // POST
    public Response moneyTransferRequest(MoneyTransferRequestCommand command) {
        return requestCommandProcessor.executeCommand(command);
    }

    // PUT
    public Response moneyTransferConfirm(MoneyTransferConfirmCommand command) {
        return confirmCommandProcessor.executeCommand(command);
    }

    // GET
    public Response moneyTransferDetails(MoneyTransferDetailsQuery query) {
        return detailsQueryProcessor.executeQuery(query);
    }

}
