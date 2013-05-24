package be.vrt.web.restdc.test.controllers;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Mike Seghers
 */
public class NoneController {
    @RequestMapping
    public void shouldNotYieldDocumentation() {

    }
}
