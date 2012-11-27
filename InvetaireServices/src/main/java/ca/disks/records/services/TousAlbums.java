package ca.disks.records.services;

import ca.disks.records.db.Inventaire;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("stock")
public class TousAlbums {

    @Context
    private UriInfo context;
    private Inventaire inventaire = Inventaire.getInstance();
    
    public TousAlbums() {
    }
    
    @GET
    @Produces("text/plain")
    public String getText() {
        return inventaire.listeAlbums();
    }
}
