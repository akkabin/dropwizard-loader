package com.yammer.dropwizard.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.loader.exception.GenericExceptionMapper;
import com.yammer.dropwizard.loader.exception.RuntimeExceptionMapper;
import com.yammer.dropwizard.loader.health.TemplateHealthCheck;

public class ServiceLoader {

	private static final Logger LOG = LoggerFactory
			.getLogger(ServiceLoader.class);

	public void loadResources(AppConfiguration configuration,
			Environment environment) {
		List<String> resources = configuration.getResources();
		for (String clazz : resources) {
			environment.addResource(makeClassInstance(clazz));
		}

	}

	public void loadHealthTemplate(AppConfiguration configuration,
			Environment environment) {
		environment.addHealthCheck(new TemplateHealthCheck(configuration
				.getTemplate()));

	}

	public void loadExceptionMapper(AppConfiguration configuration,
			Environment environment) {
		ResourceConfig jrConfig = environment.getJerseyResourceConfig();
		Set<Object> dwSingletons = jrConfig.getSingletons();
		List<Object> singletonsToRemove = new ArrayList<Object>();

		for (Object s : dwSingletons) {
			if (s instanceof ExceptionMapper
					&& s.getClass().getName()
							.startsWith("com.yammer.dropwizard.jersey.")) {
				singletonsToRemove.add(s);
			}
		}

		for (Object s : singletonsToRemove) {
			jrConfig.getSingletons().remove(s);
		}

		environment.addProvider(new RuntimeExceptionMapper());
		environment.addProvider(new GenericExceptionMapper());

	}

	@SuppressWarnings("unchecked")
	private <T> T makeClassInstance(String clazz) {
		T obj = null;
		try {
			obj = (T) Class.forName(clazz).newInstance();
		} catch (InstantiationException e) {
			LOG.error("Failed to load service instance", e);
		} catch (IllegalAccessException e) {
			LOG.error("Failed to load service instance", e);
		} catch (ClassNotFoundException e) {
			LOG.error("Class not found", e);
		}
		return obj;
	}

}
