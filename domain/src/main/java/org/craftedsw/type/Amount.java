package org.craftedsw.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Nikolay Smirnov
 */
public class Amount {

    private Currency currency;
    private BigDecimal value;

    private Amount(Currency currency, BigDecimal value) {
        this.currency = currency;
        this.value = value;
    }

    @JsonCreator
    public static Amount of(
            @JsonProperty("currency") Currency currency,
            @JsonProperty("value") BigDecimal value
    ) {
        return new Amount(currency, value);
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void withdraw(Amount withdrawAmount) {
        value = value.subtract(withdrawAmount.getValue());
    }

    public void deposit(Amount depositAmount) {
        value = value.add(depositAmount.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Amount amount = (Amount) o;
        return currency == amount.currency && Objects.equals(value, amount.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, value);
    }
}
