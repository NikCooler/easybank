package org.craftedsw.service.moneytransfer.details;

import org.craftedsw.cqrs.query.QueryProcessorBase;
import org.craftedsw.dto.MoneyTransferDetailsDto;
import org.craftedsw.model.tables.records.TransactionRecord;
import org.jooq.DSLContext;

import static org.craftedsw.model.tables.Transaction.TRANSACTION;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferDetailsQueryProcessor extends QueryProcessorBase<MoneyTransferDetailsQuery, MoneyTransferDetailsDto> {

    public MoneyTransferDetailsQueryProcessor(DSLContext readModelContext, MoneyTransferDetailsQueryValidator validator) {
        super(readModelContext, validator);
    }

    @Override
    protected MoneyTransferDetailsDto buildPayload(MoneyTransferDetailsQuery query) {
        return readModelContext.selectFrom(TRANSACTION)
                .where(TRANSACTION.TRANSACTION_ID.eq(query.getTransactionId()))
                .fetchOne(rec -> mapToDto(rec, query));
    }

    private MoneyTransferDetailsDto mapToDto(TransactionRecord rec, MoneyTransferDetailsQuery query) {
        var dto = new MoneyTransferDetailsDto(query.getTransactionId());
        dto.setTransferFrom(rec.getTransferredFrom());
        dto.setTransferTo(rec.getTransferredTo());
        dto.setTransactionStatus(rec.getStatus());
        dto.setCurrency(rec.getCurrency());
        dto.setValue(rec.getValue());
        return dto;
    }
}
