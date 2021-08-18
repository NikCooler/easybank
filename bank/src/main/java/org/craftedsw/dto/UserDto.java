package org.craftedsw.dto;

import org.craftedsw.aggregate.UserId;

/**
 * @author Nikolay Smirnov
 */
public class UserDto {

    private final UserId userId;
    private final String email;

    public UserDto(UserId userId, String email) {
        this.userId        = userId;
        this.email         = email;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}
