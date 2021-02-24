package io.tackle.controls.resources.hal;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.quarkus.rest.data.panache.runtime.hal.HalEntityWrapper;
import io.quarkus.rest.data.panache.runtime.hal.HalLink;
import io.quarkus.rest.data.panache.runtime.hal.HalLinksProvider;
import io.quarkus.runtime.annotations.RegisterForReflection;

import java.io.IOException;
import java.util.Map;

@RegisterForReflection
public class HalCollectionEnrichedWrapperJacksonSerializer extends JsonSerializer<HalCollectionEnrichedWrapper> {

    private final HalLinksProvider linksExtractor;

    public HalCollectionEnrichedWrapperJacksonSerializer() {
        this.linksExtractor = new RestEasyHalLinksProvider();
    }

    HalCollectionEnrichedWrapperJacksonSerializer(HalLinksProvider linksExtractor) {
        this.linksExtractor = linksExtractor;
    }

    @Override
    public void serialize(HalCollectionEnrichedWrapper wrapper, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {
        generator.writeStartObject();
        writeEmbedded(wrapper, generator, serializers);
        writeLinks(wrapper, generator);
        writeMetadata(wrapper, generator);
        generator.writeEndObject();
    }

    private void writeEmbedded(HalCollectionEnrichedWrapper wrapper, JsonGenerator generator, SerializerProvider serializers)
            throws IOException {
        JsonSerializer<Object> entitySerializer = serializers.findValueSerializer(HalEntityWrapper.class);

        generator.writeFieldName("_embedded");
        generator.writeStartObject();
        generator.writeFieldName(wrapper.getCollectionName());
        generator.writeStartArray(wrapper.getCollection().size());
        for (Object entity : wrapper.getCollection()) {
            entitySerializer.serialize(new HalEntityWrapper(entity), generator, serializers);
        }
        generator.writeEndArray();
        generator.writeFieldName("_metadata");
        generator.writeObject(wrapper.getMetadata());
        generator.writeEndObject();
    }

    private void writeLinks(HalCollectionEnrichedWrapper wrapper, JsonGenerator generator) throws IOException {
        Map<String, HalLink> links = linksExtractor.getLinks(wrapper.getElementType());
        links.putAll(wrapper.getLinks());
        generator.writeFieldName("_links");
        generator.writeObject(links);
    }

    private void writeMetadata(HalCollectionEnrichedWrapper wrapper, JsonGenerator generator) throws IOException {
/*
        generator.writeFieldName("_metadata");
        generator.writeObject(wrapper.getMetadata());
*/
        generator.writeObjectField("total_count", wrapper.getTotalCount());
    }
}
