package org.craftedsw.service.moneytransfer.details;

import org.craftedsw.cqrs.query.QueryProcessorBase;
import org.craftedsw.dto.MoneyTransferDetailsDto;
import org.craftedsw.model.tables.records.MoneyTransferRecord;
import org.jooq.DSLContext;

import static org.craftedsw.model.tables.MoneyTransfer.MONEY_TRANSFER;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferDetailsQueryProcessor extends QueryProcessorBase<MoneyTransferDetailsQuery, MoneyTransferDetailsDto> {

    public MoneyTransferDetailsQueryProcessor(DSLContext readModelContext, MoneyTransferDetailsQueryValidator validator) {
        super(readModelContext, validator);
    }

    @Override
    protected MoneyTransferDetailsDto buildPayload(MoneyTransferDetailsQuery query) {
        return readModelContext.selectFrom(MONEY_TRANSFER)
                .where(MONEY_TRANSFER.TRANSACTION_ID.eq(query.getTransactionId()))
                .fetchOne(rec -> mapToDto(rec, query));
    }

    private MoneyTransferDetailsDto mapToDto(MoneyTransferRecord rec, MoneyTransferDetailsQuery query) {
        var dto = new MoneyTransferDetailsDto(query.getTransactionId());
        dto.setTransferFrom(rec.getTransferredFrom());
        dto.setTransferTo(rec.getTransferredTo());
        dto.setTransferStatus(rec.getStatus());
        dto.setCurrency(rec.getCurrency());
        dto.setValue(rec.getValue());
        return dto;
    }
}
