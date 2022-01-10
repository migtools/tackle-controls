package io.tackle.controls.resources;

import io.tackle.controls.entities.Stakeholder;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.resteasy.links.LinkResource;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.Response.Status.*;

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
            if (e.getCause() instanceof ConstraintViolationException) {
                return Response.status(CONFLICT).build();
            } else {
                throw e;
            }
        }

        return Response.status(CREATED).entity(stakeholder).build();
    }
}
