package io.tackle.controls.resources;

import io.tackle.commons.resources.ListFilteredResource;
import io.tackle.controls.entities.TagType;
import org.jboss.resteasy.links.LinkResource;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("tag-type")
public class TagTypeListFilteredResource implements ListFilteredResource<TagType> {

    @Override
    public Class<TagType> getPanacheEntityType() {
        return TagType.class;
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @LinkResource(
            entityClassName = "io.tackle.controls.entities.TagType",
            rel = "list"
    )
    public Response list(@QueryParam(QUERY_PARAM_SORT) @DefaultValue(DEFAULT_VALUE_SORT) List var1,
                         @QueryParam(QUERY_PARAM_PAGE) @DefaultValue(DEFAULT_VALUE_PAGE) int var2,
                         @QueryParam(QUERY_PARAM_SIZE) @DefaultValue(DEFAULT_VALUE_SIZE) int var3,
                         @Context UriInfo var4) throws Exception {
        return ListFilteredResource.super.list(var1, var2, var3, var4, false);
    }

    @Path("")
    @GET
    @Produces({"application/hal+json"})
    public Response listHal(@QueryParam(QUERY_PARAM_SORT) @DefaultValue(DEFAULT_VALUE_SORT) List var1,
                            @QueryParam(QUERY_PARAM_PAGE) @DefaultValue(DEFAULT_VALUE_PAGE) int var2,
                            @QueryParam(QUERY_PARAM_SIZE) @DefaultValue(DEFAULT_VALUE_SIZE) int var3,
                            @Context UriInfo var4) throws Exception {
        return ListFilteredResource.super.list(var1, var2, var3, var4, true);
    }
}