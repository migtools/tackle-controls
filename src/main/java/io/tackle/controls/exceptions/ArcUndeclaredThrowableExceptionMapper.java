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
package io.tackle.controls.exceptions;

import io.quarkus.arc.ArcUndeclaredThrowableException;
import org.postgresql.util.PSQLException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Collections;

/**
 * This ExceptionMapper is inspired and based on the upcoming RestDataPanacheExceptionMapper (since Quarkus 1.13)
 * that tackle-controls can not use yet due to some issues identified with Quarkus 1.13 that prevents the upgrade.
 * Once Quarkus 1.13+ will be adopted, this ExceptionMapper will be refactored (if and how needed)
 */
@Provider
public class ArcUndeclaredThrowableExceptionMapper implements ExceptionMapper<ArcUndeclaredThrowableException> {

    private static final String PSQL_EXCEPTION_DUPLICATE_KEY_PREFIX = "ERROR: duplicate key value violates unique constraint";

    @Override
    public Response toResponse(ArcUndeclaredThrowableException e) {
        return throwableToResponse(e, e.getMessage());
    }

    private Response throwableToResponse(Throwable throwable, String message) {
        if (throwable instanceof PSQLException) {
            final String psqlMessage = throwable.getMessage();
            // if and only if a "duplicate key" PSQLException is found
            // then a JSON payload with the 'errorMessage' field is sent in the response
            if (psqlMessage != null && psqlMessage.startsWith(PSQL_EXCEPTION_DUPLICATE_KEY_PREFIX)) {
                return Response.status(Response.Status.CONFLICT.getStatusCode()).entity(Collections.singletonMap("errorMessage", PSQL_EXCEPTION_DUPLICATE_KEY_PREFIX)).build();
            }
            // otherwise the "Quarkus approach" is adopted to send out the HTTP code
            return Response.status(Response.Status.CONFLICT.getStatusCode(), message).build();
        } else {
            return throwable.getCause() != null ? this.throwableToResponse(throwable.getCause(), message) : Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), message).build();
        }
    }
}
