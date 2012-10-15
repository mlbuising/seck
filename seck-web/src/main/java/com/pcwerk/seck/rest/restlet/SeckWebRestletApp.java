package com.pcwerk.seck.rest.restlet;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.ext.freemarker.ContextTemplateLoader;
import org.restlet.routing.Router;

import com.pcwerk.seck.rest.restlet.resources.DefaultResource;
import com.pcwerk.seck.rest.restlet.resources.WebSearchResource;

import freemarker.template.Configuration;

public class SeckWebRestletApp extends Application {

	// Freemarker configuration
	private Configuration configuration;

	/**
	 * Creates a root Restlet that will receive all incoming requests.
	 */
	@Override
	public synchronized Restlet createInboundRoot() {
		// Create a router Restlet that routes requests to an appropriate
		// ServerResource object that will process the request
		Router router = new Router(getContext());

		// Set freemarker configuration
		configuration = new Configuration();
		configuration.setTemplateLoader(new ContextTemplateLoader(getContext(),
				"war:///WEB-INF/templates"));

		// TODO: Define URI mapping to classes that extend ServerResource class
		// TODO: Put ServerResource subclass in the
		// com.pcwerk.seck.rest.reslet.resources package
		// Example: router.attach("/search", SearchResource.class);
		router.attach("/search", WebSearchResource.class);
		router.attach("/search/", WebSearchResource.class);
		router.attach("/search/{searchTerm}", WebSearchResource.class);

		// Default resource handler for unmapped URIs
		router.attachDefault(DefaultResource.class);

		return router;
	}

	public Configuration getConfiguration() {
		return configuration;
	}
}