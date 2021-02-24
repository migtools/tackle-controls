package io.tackle.controls.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Used when there was no easy to understand what was going on so printing exception was crucial.
 * To use it just uncomment the @Provider annotation but just for testing purposes.
 */
//@Provider
public class VerboseExceptionMapper implements ExceptionMapper<Throwable> {

    @Override
    public Response toResponse(Throwable e) {
        e.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
