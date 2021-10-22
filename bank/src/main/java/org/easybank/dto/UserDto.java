package org.easybank.dto;

import org.easybank.aggregate.UserId;

import java.util.List;

/**
 * @author Nikolay Smirnov
 */
public class UserDto {

    private final UserId userId;
    private final String email;
    private final List<MoneyAccountDto> moneyAccounts;

    public UserDto(UserId userId, String email, List<MoneyAccountDto> moneyAccounts) {
        this.userId        = userId;
        this.email         = email;
        this.moneyAccounts = moneyAccounts;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public List<MoneyAccountDto> getMoneyAccounts() {
        return moneyAccounts;
    }
}
