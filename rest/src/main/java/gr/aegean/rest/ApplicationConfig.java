package gr.aegean.rest;

import gr.aegean.rest.resources.CitizenResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import jakarta.ws.rs.ApplicationPath;

@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        register(CitizenResource.class);
        register(JacksonFeature.class);
    }
}
