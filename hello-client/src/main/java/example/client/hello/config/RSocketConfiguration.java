package example.client.hello.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Configuration
public class RSocketConfiguration {

    @Value("${example.service.hello.hostname}")
    private String helloServiceHostname;

    @Value("${example.service.hello.port}")
    private int helloServicePort;

    @Value("${example.client.hello.clientId}")
    private String clientId;

    @Bean
    public RSocketRequester rsocketRequester() {
        return RSocketRequester.builder()
                .setupRoute("hello.setup")
                .setupMetadata(clientId, MimeType.valueOf("messaging/x.hello.clientid"))
                .setupMetadata("Hello, %s!", MimeType.valueOf("messaging/x.hello.messageformat"))
                .dataMimeType(MimeTypeUtils.TEXT_PLAIN)
                .connectTcp(helloServiceHostname, helloServicePort)
                .block();
    }
}
