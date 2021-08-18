package org.craftedsw.service.user.moneyaccount.create;

import org.craftedsw.aggregate.UserAggregateState;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.cqrs.command.CommandValidationException;
import org.craftedsw.event.MoneyAccountCreatedEvent;
import org.craftedsw.type.Amount;
import org.craftedsw.type.Currency;
import org.craftedsw.type.MoneyAccount;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static org.craftedsw.type.Currency.EUR;

/**
 * @author Nikolay Smirnov
 */
public class MoneyAccountCreateCommandValidatorTest {

    private static final UserId USER_ID = UserId.valueOf("11111111-1111-1111-1111-111111111111");

    private final MoneyAccountCreateCommandValidator validator = new MoneyAccountCreateCommandValidator();
    private final UserAggregateState userState = (UserAggregateState) USER_ID.newInstanceState();

    @Rule
    public ExpectedException expected = ExpectedException.none();

    @Test
    public void testValidate_AccountIsValid() {
        // given
        MoneyAccountCreateCommand command = mockMoneyAccountCreateCmd(USER_ID, EUR, BigDecimal.valueOf(15.55));

        // when
        boolean valid = validator.validate(command, userState);

        // then
        Assert.assertTrue(valid);
    }

    @Test
    public void testValidate_AccountNotFound() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("User is not found by id [ " + USER_ID + " ]");

        MoneyAccountCreateCommand command = mockMoneyAccountCreateCmd(USER_ID, EUR, BigDecimal.valueOf(15.55));

        // when
        boolean valid = validator.validate(command, null);

        // then
    }

    @Test
    public void testValidate_CurrencyNotFound() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Currency is required!");

        MoneyAccountCreateCommand command = mockMoneyAccountCreateCmd(USER_ID, null, BigDecimal.valueOf(15.55));

        // when
        boolean valid = validator.validate(command, userState);

        // then
    }

    @Test
    public void testValidate_MoneyAmountNotFound() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money amount is required!");

        MoneyAccountCreateCommand command = mockMoneyAccountCreateCmd(USER_ID, EUR, null);

        // when
        boolean valid = validator.validate(command, userState);

        // then
    }

    @Test
    public void testValidate_MoneyAmountIsInvalid() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money amount must NOT be less or equals '0' !");

        MoneyAccountCreateCommand command = mockMoneyAccountCreateCmd(USER_ID, EUR, BigDecimal.valueOf(-15.55));

        // when
        boolean valid = validator.validate(command, userState);

        // then
    }

    @Test
    public void testValidate_TransferRequestNotFound() {
        // given
        expected.expect(CommandValidationException.class);
        expected.expectMessage("Money account for currency [ " + EUR + " ] already exists for user [ " + USER_ID + " ]");

        var account = new MoneyAccountCreatedEvent(USER_ID);
        account.setMoneyAccount(MoneyAccount.of(Amount.of(EUR, BigDecimal.valueOf(10.30))));

        userState.apply(account);

        var command = mockMoneyAccountCreateCmd(USER_ID, EUR, BigDecimal.valueOf(15.55));

        // when
        boolean valid = validator.validate(command, userState);

        // then
    }

    private MoneyAccountCreateCommand mockMoneyAccountCreateCmd(UserId userId, Currency currency, BigDecimal value) {
        MoneyAccountCreateCommand command = new MoneyAccountCreateCommand(userId);
        command.setCurrency(currency);
        command.setValue(value);

        return command;
    }

}