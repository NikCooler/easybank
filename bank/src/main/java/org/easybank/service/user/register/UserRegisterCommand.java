package org.easybank.service.user.register;

import org.easybank.aggregate.UserId;
import org.easybank.cqrs.command.CommandBase;

/**
 * @author Nikolay Smirnov
 */
public class UserRegisterCommand extends CommandBase<UserId> {

    private String email;

    public UserRegisterCommand(UserId aggregateId) {
        super(aggregateId);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
