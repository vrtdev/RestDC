package be.vrt.web.restdc.test.controllers;

import javax.ws.rs.Path;

/**
 * @author Mike Seghers
 */
public class NoneResource {
    @Path("/none")
    public void shouldNotYieldDocumentation() {

    }
}
