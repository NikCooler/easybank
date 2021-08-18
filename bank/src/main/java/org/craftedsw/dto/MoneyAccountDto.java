package org.craftedsw.dto;

import org.craftedsw.type.Currency;

import java.math.BigDecimal;

/**
 * @author Nikolay Smirnov
 */
public class MoneyAccountDto {

    private final Currency currency;
    private final BigDecimal value;

    public MoneyAccountDto(Currency currency, BigDecimal value) {
        this.currency = currency;
        this.value    = value;
    }

    public Currency getCurrency() {
        return currency;
    }

    public BigDecimal getValue() {
        return value;
    }
}
