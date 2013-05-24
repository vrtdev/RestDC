package be.vrt.web.restdc.test.controllers;

import be.vrt.web.restdc.test.Dummy;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * @author Mike Seghers
 */
@Consumes({"application/json"})
@Produces("application/xml")
public class TestResourceWithMime {
    @Path(value = "/test2/dummies")
    @Consumes({"bladie/daa"})
    @Produces({"application/xml", "bladie/die"})
    @GET
    public List<? extends Dummy> getDummies() {
        return null;
    }

    @Path(value = "/test2/dummiesSuper")
    @GET
    public List<? super Dummy> getDummiesSuper() {
        return null;
    }

    @Path(value = "/test2/dummiesT")
    @DELETE
    public <T> List<T> getDummiesVar() {
        return null;
    }

    @Path(value = "/test2/dummiesTextend")
    @HEAD
    public <T extends Dummy> List<T> getDummiesVarExtends() {
        return null;
    }

}

