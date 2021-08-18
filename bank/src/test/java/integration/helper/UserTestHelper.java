package integration.helper;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.craftedsw.aggregate.UserId;
import org.craftedsw.type.Currency;

/**
 * @author Nikolay Smirnov
 */
public final class UserTestHelper {

    private UserTestHelper() {}

    public static HttpResponse registerUser(UserId userId, String email) {
        return Unirest.post("http://localhost:9099/user/" + userId + "/register")
                .body("{\"email\": \"" + email + "\"}")
                .asString();
    }

    public static HttpResponse createMoneyAccount(UserId userId, Currency currency, Double amount) {
        return Unirest.put("http://localhost:9099/user/" + userId + "/account/create")
                .body("{" +
                        "\"currency\": \"" + currency + "\"," +
                        "\"value\": " + amount +
                        "}")
                .asString();
    }

    public static HttpResponse getUserProfile(UserId userId) {
        return Unirest.get("http://localhost:9099/user/" + userId + "/profile")
                .asString();
    }

}
