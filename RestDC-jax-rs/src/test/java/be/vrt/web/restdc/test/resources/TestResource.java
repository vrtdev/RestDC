package be.vrt.web.restdc.test.resources;

import be.vrt.web.restdc.annotation.RestDescription;
import be.vrt.web.restdc.jaxrs.annotation.RestParameterName;
import be.vrt.web.restdc.test.Dummy;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;
import java.util.Map;

/**
 * @author Mike Seghers
 */
@Path("/test")
public class TestResource<X, Y extends Dummy> {

    @Path("/dummy/{pathVar}")
    @RestDescription("Gets a dummy")
    public Dummy getDummy(@PathParam("pathVar") String pathVar, @RestDescription("The request parameter description") @QueryParam("requestVar") String requestVar, @HeaderParam("headerVarOverride") String headerVar) {
        return null;
    }

    @Path("/dummy")
    @RestDescription("Gets a list of dummies")
    public List<Dummy> getDummies() {
        return null;
    }

    @Path(value = "/dummy")
    @POST
    public void saveDummy(@RestParameterName("dummy") Dummy dummy) {

    }

    @Path(value = "/dummy")
    @PUT
    public void updateDummy(Dummy dummy) {

    }

    @Path(value = "/dummy/{pathVar}")
    @DELETE
    public void delete(@PathParam("pathVar") String pathVarParamName) {

    }

    @Path(value = "/dummy/map")
    @GET
    public <T extends Dummy> Map<T, ? super Dummy> map() {
        return null;
    }

    @Path(value = "/dummy/map2")
    @GET
    public Map<X, Y> map2() {
        return null;
    }

    @Path(value = "/dummy/map3")
    @GET
    public <T extends X, V extends Y> Map<T, V> map3() {
        return null;
    }

    @Path(value = "/dummy/map4")
    @GET
    public Map<? extends X, ? super Y> map4() {
        return null;
    }

    @GET
    public Response simpleGet() {
        return null;
    }

    @PUT
    public Response simplePut() {
        return null;
    }

    @POST
    public Response simplePost() {
        return null;
    }

    @DELETE
    public Response simpleDelete() {
        return null;
    }

    @HEAD
    public Response simpleHead() {
        return null;
    }

    @OPTIONS
    public Response simpleOptions() {
        return null;
    }

    public void noneRequestMappingMethod() {

    }

    @Path("/")
    public void mappingMethodEmptyMapping(@Context SecurityContext securityContext) {

    }

}
