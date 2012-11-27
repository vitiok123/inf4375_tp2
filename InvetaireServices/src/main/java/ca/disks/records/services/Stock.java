/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.disks.records.services;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;

/**
 * REST Web Service
 *
 * @author Vitiok
 */
@Path("catalogue")
public class Stock {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of Stock
     */
    public Stock() {
    }

    /**
     * Retrieves representation of an instance of ca.disks.records.services.Stock
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("text/html")
    public String getHtml() {
        //TODO return proper representation object
        //throw new UnsupportedOperationException();
        return "Stock";
    }

    /**
     * PUT method for updating or creating an instance of Stock
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("text/html")
    public void putHtml(String content) {
    }
}
