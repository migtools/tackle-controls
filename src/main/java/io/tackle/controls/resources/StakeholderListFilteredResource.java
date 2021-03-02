package io.tackle.controls.resources;

import io.tackle.commons.resources.ListFilteredResource;
import io.tackle.controls.entities.Stakeholder;
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

@Path("stakeholder")
public class StakeholderListFilteredResource implements ListFilteredResource<Stakeholder> {

    @Override
    public Class<Stakeholder> getPanacheEntityType() {
        return Stakeholder.class;
    }

    @GET
    @Path("")
    @Produces({"application/json"})
    @LinkResource(
            entityClassName = "io.tackle.controls.entities.Stakeholder",
            rel = "list"
    )
    public Response list(@QueryParam("sort") List var1,
                         @QueryParam("page") @DefaultValue("0") int var2,
                         @QueryParam("size") @DefaultValue("20") int var3,
                         @QueryParam("filter") @DefaultValue("") String filter,
                         @Context UriInfo var4) throws Exception {
        return ListFilteredResource.super.list(var1, var2, var3, filter, var4, false);
    }

    @Path("")
    @GET
    @Produces({"application/hal+json"})
    public Response listHal(@QueryParam("sort") List var1,
                            @QueryParam("page") @DefaultValue("0") int var2,
                            @QueryParam("size") @DefaultValue("20") int var3,
                            @QueryParam("filter") @DefaultValue("") String filter,
                            @Context UriInfo var4) throws Exception {
        return ListFilteredResource.super.list(var1, var2, var3, filter, var4, true);
    }
}
