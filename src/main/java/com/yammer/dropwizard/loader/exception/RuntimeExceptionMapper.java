package com.yammer.dropwizard.loader.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuntimeExceptionMapper implements
		ExceptionMapper<RuntimeException> {

	private static final Logger LOG = LoggerFactory
			.getLogger(RuntimeExceptionMapper.class);

	@Override
	public Response toResponse(RuntimeException runtime) {

		// Build default response
		Response defaultResponse = Response.serverError()
				.type(MediaType.APPLICATION_JSON).entity(runtime.getMessage())
				.build();

		// Check for any specific handling
		if (runtime instanceof WebApplicationException) {
			return handleWebApplicationException(runtime, defaultResponse);
		}

		return defaultResponse;

	}

	private Response handleWebApplicationException(RuntimeException exception,
			Response defaultResponse) {
		WebApplicationException webAppException = (WebApplicationException) exception;

		// No logging
		if (webAppException.getResponse().getStatus() == 401) {
			return Response.status(Response.Status.UNAUTHORIZED)
					.type(MediaType.APPLICATION_JSON)
					.entity(exception.getMessage()).build();
		}
		if (webAppException.getResponse().getStatus() == 404) {
			return Response.status(Response.Status.NOT_FOUND)
					.type(MediaType.APPLICATION_JSON)
					.entity(exception.getMessage()).build();
		}

		return defaultResponse;
	}

}
