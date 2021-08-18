/*
 * This file is generated by jOOQ.
 */
package org.craftedsw.model.tables.pojos;


import java.io.Serializable;
import java.math.BigDecimal;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.type.Currency;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class MoneyAccount implements Serializable {

    private static final long serialVersionUID = 1012738745;

    private UserId     userId;
    private Currency   currency;
    private BigDecimal value;

    public MoneyAccount() {}

    public MoneyAccount(MoneyAccount value) {
        this.userId = value.userId;
        this.currency = value.currency;
        this.value = value.value;
    }

    public MoneyAccount(
        UserId     userId,
        Currency   currency,
        BigDecimal value
    ) {
        this.userId = userId;
        this.currency = currency;
        this.value = value;
    }

    public UserId getUserId() {
        return this.userId;
    }

    public MoneyAccount setUserId(UserId userId) {
        this.userId = userId;
        return this;
    }

    public Currency getCurrency() {
        return this.currency;
    }

    public MoneyAccount setCurrency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public MoneyAccount setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MoneyAccount other = (MoneyAccount) obj;
        if (userId == null) {
            if (other.userId != null)
                return false;
        }
        else if (!userId.equals(other.userId))
            return false;
        if (currency == null) {
            if (other.currency != null)
                return false;
        }
        else if (!currency.equals(other.currency))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        }
        else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.userId == null) ? 0 : this.userId.hashCode());
        result = prime * result + ((this.currency == null) ? 0 : this.currency.hashCode());
        result = prime * result + ((this.value == null) ? 0 : this.value.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("MoneyAccount (");

        sb.append(userId);
        sb.append(", ").append(currency);
        sb.append(", ").append(value);

        sb.append(")");
        return sb.toString();
    }
}
