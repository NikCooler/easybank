package org.craftedsw.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.craftedsw.aggregate.UserId;

import java.util.Objects;

public class Transfer {

    private final UserId withdrawFrom;
    private final UserId depositTo;

    private Transfer(UserId withdrawFrom, UserId depositTo) {
        this.withdrawFrom = withdrawFrom;
        this.depositTo = depositTo;
    }

    @JsonCreator
    public static Transfer of(@JsonProperty("withdrawFrom") UserId withdrawFrom, @JsonProperty("to") UserId depositTo) {
        return new Transfer(withdrawFrom, depositTo);
    }

    public UserId getWithdrawFrom() {
        return withdrawFrom;
    }

    public UserId getDepositTo() {
        return depositTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(withdrawFrom, transfer.withdrawFrom) && Objects.equals(depositTo, transfer.depositTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(withdrawFrom, depositTo);
    }
}
