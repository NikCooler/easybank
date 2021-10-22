package integration;

import org.easybank.BankApp;
import org.easybank.aggregate.TransactionId;
import org.easybank.aggregate.UserId;
import org.easybank.service.moneytransfer.request.MoneyTransferRequestCommand;
import kong.unirest.HttpResponse;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.easybank.type.Currency.EUR;
import static org.easybank.util.SneakyThrow.doWithRuntimeException;
import static integration.helper.MoneyTransferTestHelper.confirmTransferRequest;
import static integration.helper.MoneyTransferTestHelper.createTransferRequest;
import static integration.helper.MoneyTransferTestHelper.getMoneyTransferDetails;
import static integration.helper.TestUtils.readFromJsonFile;
import static integration.helper.UserTestHelper.createMoneyAccount;
import static integration.helper.UserTestHelper.getUserProfile;
import static integration.helper.UserTestHelper.registerUser;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * @author Nikolay Smirnov
 */
public class TransferMoneyTest {

    private BankApp app = BankApp.getInstance();

    @Before
    public void setup() {
        app.run();
    }

    @Test
    public void testMoneyTransfer() {
        // given
        UserId transferFromId = createEuroUserAccount("11111111-1111-1111-9999-111111111111", "sender@gmail.com", 100.62);
        UserId transferToId   = createEuroUserAccount("22222222-2222-2222-9999-222222222222", "recipient@gmail.com", 15.45);

        var transactionId = TransactionId.valueOf("88888888-8888-8888-9999-888888888888");
        var cmd = new MoneyTransferRequestCommand(transactionId);
        cmd.setTransferFrom(transferFromId);
        cmd.setTransferTo(transferToId);
        cmd.setCurrency(EUR);
        cmd.setValue(BigDecimal.valueOf(26.81));

        // when
        HttpResponse response = createTransferRequest(transactionId, cmd);

        // then
        assertThat(response.getBody()).isEqualTo("{\"status\":200,\"message\":\"OK\",\"payload\":null}");

        HttpResponse transferDetails = getMoneyTransferDetails(transactionId);
        assertThat(transferDetails.getBody()).isEqualTo(readFromJsonFile("testMoneyTransferProcessing.json", this.getClass()));

        assertThat(getUserProfile(transferFromId).getBody())
                .isEqualTo(readFromJsonFile("testMoneyTransferProcessingSenderProfile.json", this.getClass()));

        assertThat(getUserProfile(transferToId).getBody())
                .isEqualTo(readFromJsonFile("testMoneyTransferProcessingRecipientProfile.json", this.getClass()));

        HttpResponse transferConfirmed = confirmTransferRequest(transactionId);
        assertThat(transferConfirmed.getBody()).isEqualTo("{\"status\":200,\"message\":\"OK\",\"payload\":null}");

        doWithRuntimeException(() -> Thread.sleep(100));

        assertThat(getUserProfile(transferFromId).getBody())
                .isEqualTo(readFromJsonFile("testMoneyTransferCompletedSenderProfile.json", this.getClass()));

        assertThat(getUserProfile(transferToId).getBody())
                .isEqualTo(readFromJsonFile("testMoneyTransferCompletedRecipientProfile.json", this.getClass()));
    }

    private UserId createEuroUserAccount(String userIdAsString, String email, Double moneyAmount) {
        var userId = UserId.valueOf(userIdAsString);
        registerUser(userId, email);
        createMoneyAccount(userId, EUR, moneyAmount);

        return userId;
    }

}
