/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.disks.records.services;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author Vitiok
 */
@WebService(serviceName = "AjouterAlbum")
public class AjouterAlbum {

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Ajouter " + txt + " !";
    }
}
