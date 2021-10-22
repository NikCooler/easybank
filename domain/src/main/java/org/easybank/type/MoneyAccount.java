package org.easybank.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    public Amount getAmount() {
        return amount;
    }

    @JsonIgnore
    public Currency getCurrency() {
        return amount.getCurrency();
    }

    @JsonIgnore
    public BigDecimal getValue() {
        return amount.getValue();
    }

    public int compareValueTo(Amount amount) {
        var currentValue = this.amount.getValue();
        return currentValue.compareTo(amount.getValue());
    }

    public Amount withdraw(Amount withdrawAmount) {
        amount.withdraw(withdrawAmount);
        return amount;
    }

    public Amount deposit(Amount depositAmount) {
        amount.deposit(depositAmount);
        return amount;
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
