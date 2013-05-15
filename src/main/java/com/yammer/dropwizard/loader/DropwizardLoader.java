package com.yammer.dropwizard.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.ext.ExceptionMapper;

import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.loader.exception.GenericExceptionMapper;
import com.yammer.dropwizard.loader.exception.RuntimeExceptionMapper;
import com.yammer.dropwizard.loader.health.TemplateHealthCheck;

public class DropwizardLoader extends Service<AppConfiguration> {
	private static final String APP_NAME="Dropwizard-Loader";
	
	public static void main(String[] args) throws Exception {
		new DropwizardLoader().run(args);
	}

	@Override
	public void initialize(Bootstrap<AppConfiguration> bootstrap) {
		bootstrap.setName(APP_NAME);
	}

	@Override
	public void run(AppConfiguration configuration, Environment environment) {
		final String template = configuration.getTemplate();
		new ServiceLoader().init(configuration, environment);

		environment.addHealthCheck(new TemplateHealthCheck(template));

		// Remove all of Dropwizard's custom ExceptionMappers
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

		// Register the custom ExceptionMapper(s)
		environment.addProvider(new RuntimeExceptionMapper());
		environment.addProvider(new GenericExceptionMapper());
	}

}
