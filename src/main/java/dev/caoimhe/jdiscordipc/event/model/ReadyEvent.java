package dev.caoimhe.jdiscordipc.event.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.caoimhe.jdiscordipc.packet.impl.HandshakePacket;

/**
 * Received from the Discord client after it receives our {@link HandshakePacket}.
 */
public class ReadyEvent implements Event {
    @JsonProperty("v")
    private final int version;

    @JsonProperty("config")
    private final Configuration configuration;

    @JsonCreator
    public ReadyEvent(
        final @JsonProperty("v") int version,
        final @JsonProperty("config") Configuration configuration
    ) {
        this.version = version;
        this.configuration = configuration;
    }

    public static class Configuration {
        @JsonProperty("cdn_host")
        private final String contentDeliveryNetworkHost;

        @JsonProperty("api_endpoint")
        private final String apiEndpoint;

        @JsonProperty("environment")
        private final String environment;

        @JsonCreator
        public Configuration(
            final @JsonProperty("cdn_host") String contentDeliveryNetworkHost,
            final @JsonProperty("api_endpoint") String apiEndpoint,
            final @JsonProperty("environment") String environment
        ) {
            this.contentDeliveryNetworkHost = contentDeliveryNetworkHost;
            this.apiEndpoint = apiEndpoint;
            this.environment = environment;
        }

        public String contentDeliveryNetworkHost() {
            return this.contentDeliveryNetworkHost;
        }

        public String apiEndpoint() {
            return this.apiEndpoint;
        }

        public String environment() {
            return this.environment;
        }

        @Override
        public String toString() {
            return "Configuration { " +
                "contentDeliveryNetworkHost = \"" + this.contentDeliveryNetworkHost + "\", " +
                "apiEndpoint = \"" + this.apiEndpoint + "\", " +
                "environment = \"" + this.environment + "\"" +
                " }";
        }
    }

    public int version() {
        return this.version;
    }

    public Configuration configuration() {
        return this.configuration;
    }

    @Override
    public String toString() {
        return "ReadyEvent { version = " + this.version + ", configuration = " + this.configuration + " }";
    }
}
