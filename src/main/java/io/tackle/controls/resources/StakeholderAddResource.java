package io.tackle.controls.resources;

import io.quarkus.arc.ArcUndeclaredThrowableException;
import io.tackle.controls.entities.Stakeholder;
import org.jboss.resteasy.links.LinkResource;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.CREATED;

@Path("stakeholder")
public class StakeholderAddResource {

    @Path("/")
    @Transactional
    @POST
    @Consumes({"application/json"})
    @Produces({"application/json", "application/hal+json"})
    @LinkResource(
            entityClassName = "io.tackle.controls.entities.Stakeholder",
            rel = "add"
    )
    public Response add(Stakeholder stakeholder) {
        try {
            stakeholder.persistAndFlush();
        } catch (PersistenceException e) {
            throw new ArcUndeclaredThrowableException("Error while persisting stakeholder", e);
        }

        return Response.status(CREATED).entity(stakeholder).build();
    }
}
