package org.craftedsw.service.moneytransfer.details;

import org.craftedsw.aggregate.TransactionId;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.config.DBConfig;
import org.craftedsw.cqrs.query.QueryValidationException;
import org.craftedsw.model.tables.records.TransactionRecord;
import org.craftedsw.type.TransactionType;
import org.jooq.DSLContext;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.List;

import static org.craftedsw.model.tables.Transaction.TRANSACTION;
import static org.craftedsw.type.Currency.EUR;
import static org.craftedsw.type.TransactionStatus.PROCESSING;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferDetailsQueryValidatorTest {

    private static final TransactionId MONEY_TRANSFER_ID = TransactionId.random();

    private MoneyTransferDetailsQueryValidator validator;
    private DSLContext readModelContext;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setup() {
        readModelContext = DBConfig.getInstance().initDBContext();
        readModelContext.batchInsert(mockMoneyTransferRecords()).execute();
        validator = new MoneyTransferDetailsQueryValidator(readModelContext);
    }

    @After
    public void destroy() {
        readModelContext.deleteFrom(TRANSACTION).execute();
    }

    @Test
    public void testIsValid_MoneyTransferExists() {
        // given
        MoneyTransferDetailsQuery query = new MoneyTransferDetailsQuery(MONEY_TRANSFER_ID);
        // when
        boolean valid = validator.isValid(query);
        // then
        Assert.assertTrue(valid);
    }

    @Test
    public void testIsValid_MoneyTransferDoesNotExist() {
        // given
        var doesNotExistTransactionId = TransactionId.random();

        expected.expect(QueryValidationException.class);
        expected.expectMessage("Money transfer doesn't exist by id [ " + doesNotExistTransactionId + " ]");

        var query = new MoneyTransferDetailsQuery(doesNotExistTransactionId);

        // when
        boolean valid = validator.isValid(query);
        // then
    }

    private static List<TransactionRecord> mockMoneyTransferRecords() {
        return List.of(
                new TransactionRecord(
                        MONEY_TRANSFER_ID,
                        UserId.random(),
                        UserId.random(),
                        PROCESSING,
                        TransactionType.TRANSFER,
                        EUR,
                        BigDecimal.valueOf(10.55),
                        null,
                        System.currentTimeMillis()
                )
        );
    }
}