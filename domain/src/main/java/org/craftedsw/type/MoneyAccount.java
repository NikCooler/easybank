package org.craftedsw.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Nikolay Smirnov
 */
public final class MoneyAccount {

    private final Amount amount;

    private MoneyAccount(Amount amount) {
        this.amount = amount;
    }

    @JsonCreator
    public static MoneyAccount of(@JsonProperty("amount") Amount amount) {
        return new MoneyAccount(amount);
    }

    public Currency getCurrency() {
        return amount.getCurrency();
    }

    public BigDecimal getValue() {
        return amount.getValue();
    }

    public int compareValueTo(Amount amount) {
        var currentValue = amount.getValue();
        return currentValue.compareTo(amount.getValue());
    }

    public void withdraw(Amount withdrawAmount) {
        amount.withdraw(withdrawAmount);
    }

    public void deposit(Amount depositAmount) {
        amount.deposit(depositAmount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyAccount that = (MoneyAccount) o;
        return amount.equals(that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
