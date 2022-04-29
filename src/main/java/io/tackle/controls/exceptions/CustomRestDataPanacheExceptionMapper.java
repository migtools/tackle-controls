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
import io.quarkus.hibernate.orm.rest.data.panache.runtime.RestDataPanacheExceptionMapper;
import io.quarkus.rest.data.panache.RestDataPanacheException;
import org.jboss.logging.Logger;

import javax.annotation.Priority;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This ExceptionMapper is based on RestDataPanacheExceptionMapper. The purpose is to have a custom formatted
 * response {"errorMessage": "the error message"} rather than a generic one.
 */
@Priority(1)
@Provider
public class CustomRestDataPanacheExceptionMapper implements ExceptionMapper<RestDataPanacheException> {

    private static final Logger LOGGER = Logger.getLogger(RestDataPanacheExceptionMapper.class);

    @Override
    public Response toResponse(RestDataPanacheException exception) {
        LOGGER.warnf(exception, "Mapping an unhandled %s", RestDataPanacheException.class.getSimpleName());
        return throwableToResponse(exception, exception.getMessage());
    }

    private Response throwableToResponse(Throwable throwable, String message) {
        if (throwable instanceof ArcUndeclaredThrowableException) {
            ArcUndeclaredThrowableException e = (ArcUndeclaredThrowableException) throwable;
            return new ArcUndeclaredThrowableExceptionMapper().toResponse(e);
        }

        if (throwable instanceof org.hibernate.exception.ConstraintViolationException) {
            return Response.status(Response.Status.CONFLICT.getStatusCode(), message).build();
        }

        if (throwable instanceof javax.validation.ConstraintViolationException) {
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode(), message).build();
        }

        if (throwable.getCause() != null) {
            return throwableToResponse(throwable.getCause(), message);
        }

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), message).build();
    }

}
