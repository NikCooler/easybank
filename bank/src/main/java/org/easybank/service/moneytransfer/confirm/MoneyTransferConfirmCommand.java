package org.easybank.service.moneytransfer.confirm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.easybank.aggregate.TransactionId;
import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandBase;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferConfirmCommand extends CommandBase<TransactionId> {

    @JsonIgnore
    UserId transferFromId;

    @JsonIgnore
    UserId transferToId;

    public MoneyTransferConfirmCommand(TransactionId transactionId) {
        super(transactionId);
    }

}
