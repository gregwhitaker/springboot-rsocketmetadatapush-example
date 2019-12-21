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
     * Sets the response message format on SETUP or METADATA_PUSH.
     *
     * @param newMessageFormat message format
     * @param metadata setup metadata
     */
    @ConnectMapping("hello.setMessage")
    public void setMessage(String newMessageFormat, @Headers Map<String, Object> metadata) {
        LOG.info("Received new message format via METADATA_PUSH: {}", newMessageFormat);

        this.messageFormat = newMessageFormat;                  // setup payload
        this.clientId = metadata.get("clientId").toString();    // setup metadata
    }

    /**
     * Gets a new response message.
     *
     * @param name name to add in the response message
     * @param metadata request metadata
     * @return
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
