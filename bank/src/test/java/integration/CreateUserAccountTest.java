package integration;

import org.easybank.BankApp;
import kong.unirest.HttpResponse;
import org.easybank.aggregate.UserId;
import org.easybank.type.Currency;
import org.junit.Before;
import org.junit.Test;

import static integration.helper.TestUtils.readFromJsonFile;
import static integration.helper.UserTestHelper.createMoneyAccount;
import static integration.helper.UserTestHelper.getUserProfile;
import static integration.helper.UserTestHelper.registerUser;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Nikolay Smirnov
 */
public class CreateUserAccountTest {

    private BankApp app = BankApp.getInstance();

    @Before
    public void setup() {
        app.run();
    }

    @Test
    public void testUserRegister() {
        // given
        UserId userId = UserId.valueOf("22222222-5555-2222-2222-222222222222");

        // when
        HttpResponse response = registerUser(userId, "nik.smirnov@gmail.com");

        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("{\"status\":200,\"message\":\"OK\",\"payload\":null}");
    }

    @Test
    public void testCreateMoneyAccount() {
        // given
        UserId userId = UserId.valueOf("11111111-5555-1111-1111-111111111111");
        registerUser(userId, "nick.onkin@gmail.com");

        // when
        HttpResponse response = createMoneyAccount(userId, Currency.EUR, 10.45);

        // then
        assertThat(response.getStatus()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("{\"status\":200,\"message\":\"OK\",\"payload\":null}");

        HttpResponse userProfile = getUserProfile(userId);
        assertThat(userProfile.getStatus()).isEqualTo(200);
        assertThat(userProfile.getBody()).isEqualTo(readFromJsonFile("testUserProfile.json", this.getClass()));
    }

}
