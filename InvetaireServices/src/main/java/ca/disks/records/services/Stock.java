package ca.disks.records.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("catalogue")
public class Stock {

    @Context
    private UriInfo context;

    public Stock() {
    }
    
    @GET
    @Produces("text/plain")
    public String getText() {
        return "Stock";
    }
}
