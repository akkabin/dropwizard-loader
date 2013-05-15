package com.yammer.dropwizard.loader;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.dropwizard.config.Environment;

public class ServiceLoader {

	private static final Logger LOG = LoggerFactory
			.getLogger(ServiceLoader.class);

	public void init(AppConfiguration configuration, Environment environment) {
		List<String> resources = configuration.getResources();
		for (String clazz : resources) {
			environment.addResource(makeClassInstance(clazz));
		}
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
