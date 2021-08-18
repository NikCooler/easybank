package org.craftedsw.service.moneytransfer.details;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.config.DBConfig;
import org.craftedsw.dto.MoneyTransferDetailsDto;
import org.craftedsw.model.tables.records.TransactionRecord;
import org.craftedsw.type.TransactionType;
import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.craftedsw.type.Currency.EUR;
import static org.craftedsw.type.TransactionStatus.PROCESSING;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferDetailsQueryProcessorTest {

    private static final TransactionId TRANSACTION_ID = TransactionId.random();
    private static final UserId TRANSFER_FROM_USER_ID = UserId.random();
    private static final UserId TRANSFER_TO_USER_ID = UserId.random();

    private MoneyTransferDetailsQueryProcessor processor;
    private DSLContext readModelContext;

    @Before
    public void setup() {
        readModelContext = DBConfig.getInstance().initDBContext();
        readModelContext.executeInsert(mockMoneyTransferRecord());
        processor = new MoneyTransferDetailsQueryProcessor(readModelContext, null);
    }

    @Test
    public void testBuildPayload() {
        // given
        MoneyTransferDetailsQuery query = new MoneyTransferDetailsQuery(TRANSACTION_ID);

        // when
        MoneyTransferDetailsDto dto = processor.buildPayload(query);

        // then
        Assert.assertNotNull(dto);
        Assert.assertEquals(TRANSACTION_ID,            dto.getTransactionId());
        Assert.assertEquals(TRANSFER_FROM_USER_ID,     dto.getTransferFrom());
        Assert.assertEquals(TRANSFER_TO_USER_ID,       dto.getTransferTo());
        Assert.assertEquals(EUR,                       dto.getCurrency());
        Assert.assertEquals(BigDecimal.valueOf(10.55), dto.getValue());
    }

    private TransactionRecord mockMoneyTransferRecord() {
        return new TransactionRecord(
                TRANSACTION_ID,
                TRANSFER_FROM_USER_ID,
                TRANSFER_TO_USER_ID,
                PROCESSING,
                TransactionType.TRANSFER,
                EUR,
                BigDecimal.valueOf(10.55),
                null,
                System.currentTimeMillis()
        );
    }

}