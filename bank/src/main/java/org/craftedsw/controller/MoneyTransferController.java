package org.craftedsw.controller;

import org.craftedsw.cqrs.Response;
import org.craftedsw.service.moneytransfer.confirm.MoneyTransferConfirmCommand;
import org.craftedsw.service.moneytransfer.confirm.MoneyTransferConfirmCommandProcessor;
import org.craftedsw.service.moneytransfer.details.MoneyTransferDetailsQuery;
import org.craftedsw.service.moneytransfer.details.MoneyTransferDetailsQueryProcessor;
import org.craftedsw.service.moneytransfer.request.MoneyTransferRequestCommand;
import org.craftedsw.service.moneytransfer.request.MoneyTransferRequestCommandProcessor;

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
