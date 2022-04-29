/*
 * Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
