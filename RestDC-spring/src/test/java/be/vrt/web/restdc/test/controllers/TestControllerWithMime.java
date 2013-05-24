package be.vrt.web.restdc.test.controllers;

import be.vrt.web.restdc.test.Dummy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Mike Seghers
 */
@Controller
@RequestMapping(method = {RequestMethod.POST, RequestMethod.PUT}, consumes = {"application/json"}, produces = "application/xml")
public class TestControllerWithMime {
    @RequestMapping(value = "/test2/dummies", consumes = {"bladie/daa"}, produces = {"application/xml", "bladie/die"}, method = RequestMethod.GET)
    public List<? extends Dummy> getDummies() {
        return null;
    }

    @RequestMapping(value = "/test2/dummiesSuper", method = RequestMethod.GET)
    public List<? super Dummy> getDummiesSuper() {
        return null;
    }

    @RequestMapping(value = "/test2/dummiesT", method = RequestMethod.DELETE)
    public <T> List<T> getDummiesVar() {
        return null;
    }

    @RequestMapping(value = "/test2/dummiesTextend", method = RequestMethod.HEAD)
    public <T extends Dummy> List<T> getDummiesVarExtends() {
        return null;
    }

}

