package gguip1.community.global.infra;

import org.junit.jupiter.api.Test;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;

public class ParameterStoreUnitTest {

    @Test
    void getParameterFromSsm() {
        SsmClient ssm = SsmClient.builder()
                .region(Region.AP_NORTHEAST_2)
                .build();

        var res = ssm.getParameter(
                GetParameterRequest.builder()
                        .name("/ktb-community-backend-ps/database/url")
                        .withDecryption(true)
                        .build()
        );

        System.out.println("Parameter Value: " + res.parameter().value());
    }
}
