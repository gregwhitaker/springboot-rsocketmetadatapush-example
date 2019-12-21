package example.service.hello.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.rsocket.annotation.ConnectMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Controller that generates hello messages.
 */
@Controller
public class HelloController {
    private static final Logger LOG = LoggerFactory.getLogger(HelloController.class);

    private String clientId;
    private volatile String messageFormat;

    /**
     * Handles the SETUP frame on initial connection.
     *
     * @param metadata setup metadata
     */
    @ConnectMapping("hello.setup")
    public Mono<Void> setup(@Headers Map<String, Object> metadata) {
        this.messageFormat = metadata.get("messageFormat").toString();
        this.clientId = metadata.get("clientId").toString();
        return Mono.empty();
    }

    /**
     * Handles METADATA_PUSH frames to update the message format.
     *
     * @param metadata metadata
     */
    @ConnectMapping
    public Mono<Void> metadataUpdate(@Headers Map<String, Object> metadata) {
        this.messageFormat = metadata.get("messageFormat").toString();
        return Mono.empty();
    }

    /**
     * Gets a new response message.
     *
     * @param name name to add in the response message
     * @param metadata request metadata
     * @return hello message
     */
    @MessageMapping("hello")
    public Mono<String> hello(String name, @Headers Map<String, Object> metadata) {
        return Mono.fromSupplier(() -> {
            if (clientId != null) {
                return String.format(messageFormat + " [clientId: '%s']", name, clientId);
            } else {
                return String.format(messageFormat, name);
            }
        });
    }
}
