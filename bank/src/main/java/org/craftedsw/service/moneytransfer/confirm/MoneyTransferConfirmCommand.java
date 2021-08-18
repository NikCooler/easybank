package org.craftedsw.service.moneytransfer.confirm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.command.CommandBase;

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
