package org.craftedsw.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author Nikolay Smirnov
 */
public final class MoneyAccount {

    private final Currency currency;
    private final BigDecimal value;

    private MoneyAccount(Currency currency, BigDecimal value) {
        this.currency = currency;
        this.value    = value;
    }

    @JsonCreator
    public static MoneyAccount of(@JsonProperty("currency") Currency currency, @JsonProperty("value") BigDecimal value) {
        return new MoneyAccount(currency, value);
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoneyAccount that = (MoneyAccount) o;
        return currency == that.currency &&
                value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency, value);
    }
}
