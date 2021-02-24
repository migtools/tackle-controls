package io.tackle.controls.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class BusinessServiceDTO {
    public String name;
    public String description;
    public String ownerDisplayName;

/*    public BusinessServiceDTO(String name, String description, @ProjectedFieldName("owner.displayName") String ownerDisplayName) {
        this.name = name;
        this.description = description;
        this.ownerDisplayName = ownerDisplayName;
    }*/
}
