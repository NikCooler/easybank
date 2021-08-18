package org.craftedsw.event;

import org.craftedsw.aggregate.UserId;
import org.craftedsw.type.MoneyAccount;

/**
 * @author Nikolay Smirnov
 */
public class MoneyAccountCreatedEvent extends EventBase<UserId> {
    private static final long serialVersionUID = 1L;

    private MoneyAccount moneyAccount;

    public MoneyAccountCreatedEvent(UserId aggregateId) {
        super(aggregateId);
    }

    public MoneyAccount getMoneyAccount() {
        return moneyAccount;
    }

    public void setMoneyAccount(MoneyAccount moneyAccount) {
        this.moneyAccount = moneyAccount;
    }

    /**
     * This constructor is used only for deserialization
     */
    public MoneyAccountCreatedEvent(long eventId, String aggregateId, int aggregateVersion, long eventTimestamp) {
        super(eventId, UserId.valueOf(aggregateId), aggregateVersion, eventTimestamp);
    }

}
