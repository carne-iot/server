package ar.edu.itba.iot.carne_iot.server.web.controller.dtos.entities;

import ar.edu.itba.iot.carne_iot.server.models.Device;
import ar.edu.itba.iot.carne_iot.server.models.User;
import ar.edu.itba.iot.carne_iot.server.services.DeviceService;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.deserializers.UrlSafeBase64ToLongDeserializer;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers.Java8InstantSerializer;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers.LongToUrlSafeBase64Serializer;
import ar.edu.itba.iot.carne_iot.server.web.support.data_transfer.json.serializers.URISerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.math.BigDecimal;
import java.net.URI;
import java.time.Instant;

/**
 * Data transfer object for {@link Device} class.
 */
public class DeviceDto {

    @JsonProperty
    @JsonSerialize(using = LongToUrlSafeBase64Serializer.class)
    @JsonDeserialize(using = UrlSafeBase64ToLongDeserializer.class)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private BigDecimal temperature;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = Java8InstantSerializer.class)
    private Instant lastTemperatureUpdate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Device.State state;

    @SuppressWarnings("unused")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonSerialize(using = URISerializer.class)
    private URI locationUrl;


    public DeviceDto() {
        // For Jersey
    }

    /**
     * Private constructor that sets up common properties.
     *
     * @param device      The device from which the dto will be built.
     * @param locationUrl The location url (in {@link URI} format) of the given {@link Device}.
     */
    /* package */ DeviceDto(Device device, URI locationUrl) {
        this.id = device.getId();
        this.temperature = device.getTemperature();
        this.lastTemperatureUpdate = device.getLastTemperatureUpdate();
        this.state = device.getState();
        this.locationUrl = locationUrl;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public Instant getLastTemperatureUpdate() {
        return lastTemperatureUpdate;
    }

    public Device.State getState() {
        return state;
    }

    public URI getLocationUrl() {
        return locationUrl;
    }
}
