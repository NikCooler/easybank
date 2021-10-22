package org.easybank.service.moneytransfer.request;

import org.easybank.aggregate.TransactionId;
import org.easybank.aggregate.UserAggregateState;
import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandValidationException;
import org.easybank.event.MoneyAccountCreatedEvent;
import org.easybank.service.moneytransfer.MoneyTransferValidator;
import org.easybank.type.Amount;
import org.easybank.type.Currency;
import org.easybank.type.MoneyAccount;
import org.easybank.writelane.EventStoreService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;

import java.math.BigDecimal;

import static org.easybank.type.Currency.EUR;
import static java.util.Optional.ofNullable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * @author Nikolay Smirnov
 */
public class MoneyTransferRequestCommandValidatorTest {

    private static final TransactionId TRANSFER_ID = TransactionId.valueOf("88888888-8888-8888-8888-888888888888");

    private static final UserId TRANSFER_FROM_USER_ID = UserId.valueOf("11111111-1111-1111-1111-111111111111");
    private static final UserId TRANSFER_TO_USER_ID = UserId.valueOf("22222222-2222-2222-2222-222222222222");

    private MoneyTransferRequestCommandValidator validator;
    private UserAggregateState transferFromUserState;
    private UserAggregateState transferToUserState;

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        EventStoreService eventStoreService = Mockito.mock(EventStoreService.class);

        this.transferFromUserState = (UserAggregateState) TRANSFER_FROM_USER_ID.newInstanceState();
        this.transferToUserState   = (UserAggregateState) TRANSFER_TO_USER_ID.newInstanceState();

        when(eventStoreService.retrieveAggregate(eq(TRANSFER_FROM_USER_ID))).thenReturn(this.transferFromUserState);
        when(eventStoreService.retrieveAggregate(eq(TRANSFER_TO_USER_ID))).thenReturn(this.transferToUserState);

        this.validator = new MoneyTransferRequestCommandValidator(eventStoreService, new MoneyTransferValidator());
    }

    @Test
    public void testValidate_TransferRequestIsValid() {
        // given
        transferFromUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_FROM_USER_ID, 15.50));
        transferToUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_TO_USER_ID, 4.40));

        // when
        boolean valid = validator.validate(buildCommand(TRANSFER_FROM_USER_ID, TRANSFER_TO_USER_ID, EUR, 10.05), null);

        // then
        Assert.assertTrue(valid);
    }

    @Test
    public void testValidate_TransferRequestAlreadyExists() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money transfer request already exists by id [ " + TRANSFER_ID + " ]");

        // when
        validator.validate(buildCommand(null, null, null, null), TRANSFER_ID.newInstanceState());
        // then
    }

    @Test
    public void testValidate_TransferUserFromIsEmpty() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Field 'transferFrom' must NOT be empty");

        // when
        validator.validate(buildCommand(null, TRANSFER_TO_USER_ID, EUR, 10.05), null);
        // then
    }

    @Test
    public void testValidate_TransferUserToIsEmpty() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Field 'transferTo' must NOT be empty");

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, null, EUR, 10.05), null);
        // then
    }

    @Test
    public void testValidate_CurrencyIsEmpty() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Field 'currency' must NOT be empty");

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, TRANSFER_TO_USER_ID, null, 10.05), null);
        // then
    }

    @Test
    public void testValidate_ValueIsEmpty() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Field 'value' must NOT be empty");

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, TRANSFER_TO_USER_ID, EUR, null), null);
        // then
    }

    @Test
    public void testValidate_UserFromIsNotFound() {
        // given
        UserId userFrom = UserId.random();
        expected.expect(CommandValidationException.class);
        expected.expectMessage("User 'from' is not found by id [ " + userFrom + " ]");

        // when
        validator.validate(buildCommand(userFrom, TRANSFER_TO_USER_ID, EUR, 10.35), null);
        // then
    }

    @Test
    public void testValidate_UserToIsNotFound() {
        // given
        UserId userTo = UserId.random();
        expected.expect(CommandValidationException.class);
        expected.expectMessage("User 'to' is not found by id [ " + userTo + " ]");

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, userTo, EUR, 10.35), null);
        // then
    }


    @Test
    public void testValidate_UsersAreTheSame() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Users are the same!");

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, TRANSFER_FROM_USER_ID, EUR, 10.15), null);
        // then
    }

    @Test
    public void testValidate_MoneyAmountMustBeGreaterThenZero() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money amount must be greater than zero!");

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, TRANSFER_TO_USER_ID, EUR, -5.50), null);
        // then
    }

    @Test
    public void testValidate_IncorrectMoneyAmountValue() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money amount is in incorrect format. Must be '#.##'");

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, TRANSFER_TO_USER_ID, EUR, 10.2456002), null);
        // then
    }

    @Test
    public void testValidate_UserFromDoesNotHaveMoneyAccount() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money transfer [ " + TRANSFER_ID + " ] failed due to 'Sender' [ " + TRANSFER_FROM_USER_ID + " ] " +
                "doesn't have money account [ EUR ]");

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, TRANSFER_TO_USER_ID, EUR, 10.30), null);
        // then
    }

    @Test
    public void testValidate_UserFromDoesNotHaveEnoughMoney() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money transfer [ " + TRANSFER_ID + " ] failed due to 'Sender' [ " + TRANSFER_FROM_USER_ID + " ] " +
                "doesn't have enough money [ EUR ]");
        transferFromUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_FROM_USER_ID, 3.40));

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, TRANSFER_TO_USER_ID, EUR, 10.50), null);
        // then
    }

    @Test
    public void testValidate_UserToDoesNotHaveMoneyAccount() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money transfer [ " + TRANSFER_ID + " ] failed due to 'Receiver' [ " + TRANSFER_TO_USER_ID + " ] " +
                "doesn't have money account [ EUR ]");
        transferFromUserState.apply(mockMoneyAccountCreatedEvt(TRANSFER_FROM_USER_ID, 15.50));

        // when
        validator.validate(buildCommand(TRANSFER_FROM_USER_ID, TRANSFER_TO_USER_ID, EUR, 10.30), null);
        // then
    }

    private MoneyTransferRequestCommand buildCommand(UserId fromId, UserId toId, Currency currency, Double value) {
        MoneyTransferRequestCommand command = new MoneyTransferRequestCommand(TRANSFER_ID);
        command.setTransferFrom(fromId);
        command.setTransferTo(toId);
        command.setCurrency(currency);
        command.setValue(ofNullable(value).map(BigDecimal::valueOf).orElse(null));

        return command;
    }

    private MoneyAccountCreatedEvent mockMoneyAccountCreatedEvt(UserId userId, Double moneyAmount) {
        MoneyAccountCreatedEvent account = new MoneyAccountCreatedEvent(userId);
        account.setMoneyAccount(MoneyAccount.of(Amount.of(EUR, BigDecimal.valueOf(moneyAmount))));

        return account;
    }

}