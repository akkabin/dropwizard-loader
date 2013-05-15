package com.yammer.dropwizard.loader.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		ResponseBuilder builder = Response.status(Status.INTERNAL_SERVER_ERROR);
		builder.type(MediaType.APPLICATION_JSON).entity(
				generateErrorMessage(exception));
		return builder.build();
	}

	private String generateErrorMessage(Throwable exception) {
		String errorKey = exception.getMessage();
		return errorKey;
	}

}
