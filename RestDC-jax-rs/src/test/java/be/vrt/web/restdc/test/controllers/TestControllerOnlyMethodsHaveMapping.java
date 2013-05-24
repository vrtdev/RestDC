package be.vrt.web.restdc.test.controllers;

import javax.ws.rs.Path;

/**
 * @author Mike Seghers
 */
public class TestControllerOnlyMethodsHaveMapping {
    @Path("/some/url")
    public void method() {

    }
}
