package com.yammer.dropwizard.loader;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

public class DropwizardLoader extends Service<AppConfiguration> {
	private static final String APP_NAME = "Dropwizard-Loader";
	private static final ServiceLoader loader = new ServiceLoader();

	@Override
	public void initialize(Bootstrap<AppConfiguration> bootstrap) {
		bootstrap.setName(APP_NAME);
	}

	@Override
	public void run(AppConfiguration configuration, Environment environment) {
		loader.loadResources(configuration, environment);
		loader.loadExceptionMapper(configuration, environment);
		loader.loadHealthTemplate(configuration, environment);
	}
	
	public static void main(String[] args) throws Exception {
		new DropwizardLoader().run(args);
	}


}
