package io.tackle.controls.resources.hal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.tackle.controls.resources.responses.Metadata;
import io.quarkus.rest.data.panache.runtime.hal.HalCollectionWrapper;

import java.util.Collection;

@JsonSerialize(using = HalCollectionEnrichedWrapperJacksonSerializer.class)
public class HalCollectionEnrichedWrapper extends HalCollectionWrapper {

    /**
     * TODO Decide which one to keep: metadata object or straight totalCount field?
     * I prefer metadata object because it's inside the '_embedded' component of the
     * HAL response and it contains the metadata about the embedded collection
     * referenced by the name of resources type with the collection.
     */
    private final Metadata metadata;
    private final long totalCount;

    public HalCollectionEnrichedWrapper(Collection<Object> collection, Class<?> elementType, String collectionName, long totalCount) {
        super(collection, elementType, collectionName);
        metadata = Metadata.withTotalCount(totalCount);
        this.totalCount = totalCount;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public long getTotalCount() {
        return totalCount;
    }

}
