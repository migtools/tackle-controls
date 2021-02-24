package io.tackle.controls.resources.responses;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Metadata {
    public long totalCount;

    private Metadata() {}

    public static Metadata withTotalCount(long totalCount) {
        Metadata metadata = new Metadata();
        metadata.totalCount = totalCount;
        return metadata;
    }
}
