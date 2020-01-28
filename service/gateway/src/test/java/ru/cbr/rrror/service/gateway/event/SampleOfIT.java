package ru.cbr.rrror.service.gateway.event;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import ru.cbr.rrror.service.gateway.GatewayApplication;
import ru.cbr.rrror.service.gateway.config.ApplicationConfig;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = {GatewayApplication.class},
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)
@ActiveProfiles("dev")
public class SampleOfIT {

    @Autowired
    private ApplicationConfig.Config config;

    @Test
    public void testOfTest() {
        Assert.assertTrue(true);

        log.debug("config: " + config.toString());
    }
}
