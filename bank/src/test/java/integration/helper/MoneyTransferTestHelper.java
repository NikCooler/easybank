package integration.helper;

import org.easybank.aggregate.TransactionId;
import org.easybank.service.moneytransfer.request.MoneyTransferRequestCommand;
import io.javalin.plugin.json.JavalinJson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

/**
 * @author Nikolay Smirnov
 */
public final class MoneyTransferTestHelper {

    private MoneyTransferTestHelper() {}

    public static HttpResponse createTransferRequest(TransactionId transactionId, MoneyTransferRequestCommand cmd) {
        return Unirest.post("http://localhost:9099/transfer/" + transactionId + "/request")
                .body(JavalinJson.toJson(cmd))
                .asString();
    }

    public static HttpResponse confirmTransferRequest(TransactionId transactionId) {
        return Unirest.put("http://localhost:9099/transfer/" + transactionId + "/confirm")
                .body("{}")
                .asString();
    }
    public static HttpResponse getMoneyTransferDetails(TransactionId transactionId) {
        return Unirest.get("http://localhost:9099/transfer/" + transactionId + "/details")
                .asString();
    }

}
