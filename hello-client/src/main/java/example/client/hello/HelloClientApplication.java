package example.client.hello;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.Unpooled;
import io.rsocket.metadata.CompositeMetadataFlyweight;
import io.rsocket.util.ByteBufPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;

import static picocli.CommandLine.Parameters;
import static picocli.CommandLine.populateCommand;

@SpringBootApplication
public class HelloClientApplication {
    private static final Logger LOG = LoggerFactory.getLogger(HelloClientApplication.class);

    public static void main(String... args) {
        SpringApplication.run(HelloClientApplication.class, args);
    }

    /**
     * Runs the application.
     */
    @Component
    public class Runner implements CommandLineRunner {

        @Autowired
        private RSocketRequester rSocketRequester;

        @Override
        public void run(String... args) throws Exception {
            ClientArguments params = populateCommand(new ClientArguments(), args);

            LOG.info("Sending hello request 1...");

            // Sending initial hello request
            String helloResponse1 = rSocketRequester.route("hello")
                    .data(params.name)
                    .retrieveMono(String.class)
                    .block();

            LOG.info("Response: {}", helloResponse1);

            updateMessageFormat("Bonjour, %s!");

            LOG.info("Sending hello request 2...");

            // Sending initial hello request
            String helloResponse2 = rSocketRequester.route("hello")
                    .data(params.name)
                    .retrieveMono(String.class)
                    .block();

            LOG.info("Response: {}", helloResponse2);
        }

        /**
         * Sends a metadata push to update the message format.
         *
         * @param messageFormat new message format
         */
        private void updateMessageFormat(String messageFormat) {
            LOG.info("Pushing new hello message format via METADATA_PUSH: {}", messageFormat);

            // Create composite metadata to send
            CompositeByteBuf metadataByteBuf = ByteBufAllocator.DEFAULT.compositeBuffer();
            CompositeMetadataFlyweight.encodeAndAddMetadata(
                    metadataByteBuf,
                    ByteBufAllocator.DEFAULT,
                    "messaging/x.hello.messageformat",
                    ByteBufAllocator.DEFAULT.buffer().writeBytes(messageFormat.getBytes()));

            // Send the composite metadata
            rSocketRequester.rsocket()
                    .metadataPush(ByteBufPayload.create(Unpooled.EMPTY_BUFFER, metadataByteBuf))
                    .block();
        }
    }

    /**
     * Hello client command line arguments.
     */
    public static class ClientArguments {

        /**
         * "name" argument to send to the method
         */
        @Parameters(index = "0", arity = "1", defaultValue = "name argument for method")
        public String name;
    }
}
