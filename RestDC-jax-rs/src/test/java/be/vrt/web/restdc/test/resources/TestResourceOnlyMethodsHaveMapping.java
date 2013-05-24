package be.vrt.web.restdc.test.resources;

import javax.ws.rs.Path;

/**
 * @author Mike Seghers
 */
public class TestResourceOnlyMethodsHaveMapping {
    @Path("/some/url")
    public void method() {

    }
}
