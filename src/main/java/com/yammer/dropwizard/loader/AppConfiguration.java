package com.yammer.dropwizard.loader;

import java.util.List;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

public class AppConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String template;

    @NotEmpty
    @JsonProperty
    private String defaultName = "Stranger";
    
    @NotEmpty
    @JsonProperty
    private List<String> resources;

    public String getTemplate() {
        return template;
    }

    public String getDefaultName() {
        return defaultName;
    }

	/**
	 * @return the resources
	 */
	public List<String> getResources() {
		return resources;
	}
}
