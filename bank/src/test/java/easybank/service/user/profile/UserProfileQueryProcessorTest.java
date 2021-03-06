package org.easybank.service.user.profile;

import org.easybank.aggregate.UserId;
import org.easybank.config.DBConfig;
import org.easybank.dto.MoneyAccountDto;
import org.easybank.dto.UserDto;
import org.easybank.model.tables.records.MoneyAccountRecord;
import org.easybank.model.tables.records.UserRecord;
import org.jooq.DSLContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.easybank.type.Currency.EUR;

/**
 * @author Nikolay Smirnov
 */
public class UserProfileQueryProcessorTest {

    private static final UserId USER_ID = UserId.random();

    private UserProfileQueryProcessor processor;
    private DSLContext                readModelContext;

    @Before
    public void setup() {
        readModelContext = DBConfig.getInstance().initDBContext();
        readModelContext.batchInsert(
                mockUserRecord(),
                mockMoneyAccountRecord()
        ).execute();
        processor = new UserProfileQueryProcessor(readModelContext, null);
    }

    @Test
    public void testBuildPayload() {
        // given
        UserProfileQuery query = new UserProfileQuery(USER_ID);

        // when
        UserDto dto = processor.buildPayload(query);

        // then
        Assert.assertNotNull(dto);
        Assert.assertEquals(USER_ID,                 dto.getUserId());
        Assert.assertEquals("nik.smirnov@gmail.com", dto.getEmail());

        Assert.assertEquals(1, dto.getMoneyAccounts().size());
        MoneyAccountDto moneyAccountDto = dto.getMoneyAccounts().get(0);
        Assert.assertEquals(EUR, moneyAccountDto.getCurrency());
        Assert.assertEquals(BigDecimal.valueOf(10.55), moneyAccountDto.getValue());
    }

    private UserRecord mockUserRecord() {
        return new UserRecord(USER_ID, "nik.smirnov@gmail.com");
    }

    private MoneyAccountRecord mockMoneyAccountRecord() {
        return new MoneyAccountRecord(USER_ID, EUR, BigDecimal.valueOf(10.55));
    }
}