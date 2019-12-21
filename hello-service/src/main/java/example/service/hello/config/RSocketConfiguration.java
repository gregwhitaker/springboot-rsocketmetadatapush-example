package example.service.hello.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.codec.StringDecoder;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;
import org.springframework.util.MimeTypeUtils;

@Configuration
public class RSocketConfiguration {

    @Bean
    public RSocketMessageHandler rsocketMessageHandler() {
        RSocketMessageHandler handler = new RSocketMessageHandler();
        handler.setRSocketStrategies(rsocketStrategies());
        return handler;
    }

    @Bean
    public RSocketStrategies rsocketStrategies() {
        return RSocketStrategies.builder()
                .metadataExtractorRegistry(registry -> {
                    registry.metadataToExtract(MimeTypeUtils.TEXT_PLAIN, String.class, "clientId");     // SETUP metadata
                })
                .decoders(decoders -> {
                    decoders.add(StringDecoder.allMimeTypes());
                })
                .build();
    }
}
