package org.easybank.service.moneytransfer.confirm;

import org.easybank.aggregate.TransactionAggregateState;
import org.easybank.aggregate.TransactionId;
import org.easybank.aggregate.UserAggregateState;
import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandValidationException;
import org.easybank.event.MoneyAccountCreatedEvent;
import org.easybank.event.TransactionCompletedEvent;
import org.easybank.event.TransactionStartedEvent;
import org.easybank.service.moneytransfer.MoneyTransferValidator;
import org.easybank.type.*;
import org.easybank.writelane.EventStoreService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.easybank.type.Currency.EUR;
import static org.easybank.type.TransactionStatus.COMPLETED;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferConfirmCommandValidatorTest {

    private static final TransactionId TRANSFER_ID = TransactionId.valueOf("88888888-8888-8888-8888-888888888888");

    private static final UserId TRANSFER_FROM_USER_ID = UserId.valueOf("11111111-1111-1111-1111-111111111111");
    private static final UserId TRANSFER_TO_USER_ID   = UserId.valueOf("22222222-2222-2222-2222-222222222222");

    private MoneyTransferConfirmCommandValidator validator;
    private TransactionAggregateState transactionState;
    private UserAggregateState transferFromUserState;
    private UserAggregateState                   transferToUserState;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        // prepare money transaction state
        this.transactionState = (TransactionAggregateState) TRANSFER_ID.newInstanceState();

        var eventStoreService = Mockito.mock(EventStoreService.class);

        this.transferFromUserState = (UserAggregateState) TRANSFER_FROM_USER_ID.newInstanceState();
        this.transferToUserState   = (UserAggregateState) TRANSFER_TO_USER_ID.newInstanceState();

        when(eventStoreService.retrieveAggregate(eq(TRANSFER_FROM_USER_ID))).thenReturn(this.transferFromUserState);
        when(eventStoreService.retrieveAggregate(eq(TRANSFER_TO_USER_ID))).thenReturn(this.transferToUserState);

        this.validator = new MoneyTransferConfirmCommandValidator(eventStoreService, new MoneyTransferValidator());
    }

    @Test
    public void testValidate_TransferIsValid() {
        // given
        transactionState.apply(mockTransactionStartedEvt());
        transferFromUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_FROM_USER_ID, 15.50));
        transferToUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_TO_USER_ID, 4.40));

        // when
        boolean valid = validator.validate(new MoneyTransferConfirmCommand(TRANSFER_ID), transactionState);

        // then
        Assert.assertTrue(valid);
    }

    @Test
    public void testValidate_TransferRequestNotFound() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money transfer request is not found by id [ " + TRANSFER_ID + " ]");

        // when
        boolean valid = validator.validate(new MoneyTransferConfirmCommand(TRANSFER_ID), null);

        // then
    }

    @Test
    public void testValidate_UserFromAccountNotFound() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money transfer [ " + TRANSFER_ID + " ]" + " failed due to 'Sender' [ " + TRANSFER_FROM_USER_ID + " ] " +
                "doesn't have money account [ " + EUR + " ]");

        transactionState.apply(mockTransactionStartedEvt());

        // when
        boolean valid = validator.validate(new MoneyTransferConfirmCommand(TRANSFER_ID), transactionState);

        // then
    }

    @Test
    public void testValidate_UserFromHasNoEnoughMoney() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money transfer [ " + TRANSFER_ID + " ]" + " failed due to 'Sender' [ " + TRANSFER_FROM_USER_ID + " ] " +
                "doesn't have enough money [ " + EUR + " ]");

        transferFromUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_FROM_USER_ID, 8.15));
        transferToUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_TO_USER_ID, 4.40));
        transactionState.apply(mockTransactionStartedEvt());

        // when
        boolean valid = validator.validate(new MoneyTransferConfirmCommand(TRANSFER_ID), transactionState);

        // then
    }

    @Test
    public void testValidate_UserToAccountNotFound() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money transfer [ " + TRANSFER_ID + " ]" + " failed due to 'Receiver' [ " + TRANSFER_TO_USER_ID + " ] " +
                "doesn't have money account [ " + EUR + " ]");

        transferFromUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_FROM_USER_ID, 15.50));
        transactionState.apply(mockTransactionStartedEvt());

        // when
        boolean valid = validator.validate(new MoneyTransferConfirmCommand(TRANSFER_ID), transactionState);

        // then
    }

    @Test
    public void testValidate_TransferRequestAlreadyConfirmed() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money transfer request by id [ " + TRANSFER_ID + " ] has incorrect status [ " + COMPLETED + " ]");

        transactionState.apply(mockTransactionStartedEvt());
        transferFromUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_FROM_USER_ID, 15.50));
        transferToUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_TO_USER_ID, 4.40));
        transactionState.apply(new TransactionCompletedEvent(TRANSFER_ID));

        // when
        boolean valid = validator.validate(new MoneyTransferConfirmCommand(TRANSFER_ID), transactionState);

        // then
    }

    private TransactionStartedEvent mockTransactionStartedEvt() {
        var event = new TransactionStartedEvent(TRANSFER_ID);

        var transactionState = TransactionState.of(TransactionType.TRANSFER, TransactionStatus.PROCESSING);
        var transfer = Transfer.of(TRANSFER_FROM_USER_ID, TRANSFER_TO_USER_ID);
        var transaction = Transaction.of(transactionState, transfer);
        var amount = Amount.of(EUR, BigDecimal.valueOf(10.0));

        event.setAmount(amount);
        event.setTransaction(transaction);

        return event;
    }

    private MoneyAccountCreatedEvent mockMoneyAccountCreatedEvt(UserId userId, Double moneyAmount) {
        MoneyAccountCreatedEvent account = new MoneyAccountCreatedEvent(userId);
        account.setMoneyAccount(MoneyAccount.of(Amount.of(EUR, BigDecimal.valueOf(moneyAmount))));

        return account;
    }

}