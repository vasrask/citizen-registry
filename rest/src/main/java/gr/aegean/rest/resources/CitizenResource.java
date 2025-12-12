package gr.aegean.rest.resources;

import gr.aegean.domain.Citizen;
import gr.aegean.rest.repositories.CitizenRepository;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;
import java.util.Random;

@Path("citizens")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CitizenResource {

    @GET
    public Response getAll() {
        return Response.ok(CitizenRepository.findAll().values()).build();
    }

    @GET
    @Path("{registryNumber}")
    public Response getOne(@PathParam("registryNumber") String registryNumber) {
        Citizen c = CitizenRepository.findByRegistryNumber(registryNumber);
        if (c == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Citizen not found")
                    .build();
        }
        return Response.ok(c).build();
    }


    @POST
    public Response create(Citizen c, @Context UriInfo uriInfo) {
        c.generateRegistryNumber();
        Citizen saved = CitizenRepository.save(c);
        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @PUT
    @Path("{registryNumber}")
    public Citizen update(@PathParam("registryNumber") String registryNumber, Citizen c) {
        c.setRegistryNumber(registryNumber);
        CitizenRepository.save(c);
        return c;
    }


    @DELETE
    @Path("{registryNumber}")
    public Response delete(@PathParam("registryNumber") String registryNumber) {
        boolean deleted = CitizenRepository.delete(registryNumber);
        if (!deleted) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Citizen not found")
                    .build();
        }
        return Response.ok("Deleted").build();    }

}