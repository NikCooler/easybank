package org.easybank.aggregate;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jooq.Converter;

import java.util.UUID;

/**
 * @author Nikolay Smirnov
 */
public class UserId extends AggregateIdBase<UserId> {

    public UserId(UUID pId) {
        super(pId);
    }

    public UserId(String pId) {
        super(UUID.fromString(pId));
    }

    @JsonCreator
    public static UserId valueOf(@JsonProperty("userId") String userId) {
        return new UserId(userId);
    }

    public static UserId random() {
        return new UserId(UUID.randomUUID());
    }

    @Override
    public AggregateStateBase<UserId> newInstanceState() {
        return new UserAggregateState(this);
    }

    public static class JooqUserIdConverter implements Converter<String, UserId> {
        private static final long serialVersionUID = 1L;

        @Override
        public UserId from(String s) {
            return UserId.valueOf(s);
        }

        @Override
        public String to(UserId userId) {
            return userId.toString();
        }

        @Override
        public Class<String> fromType() {
            return String.class;
        }

        @Override
        public Class<UserId> toType() {
            return UserId.class;
        }
    }
}
