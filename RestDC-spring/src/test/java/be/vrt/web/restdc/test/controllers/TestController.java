package be.vrt.web.restdc.test.controllers;

import be.vrt.web.restdc.annotation.RestDescription;
import be.vrt.web.restdc.test.Dummy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @author Mike Seghers
 */
@Controller
@RequestMapping("/test")
public class TestController<X, Y extends Dummy> {

    @ResponseBody
    @RequestMapping("/dummy/{pathVar}")
    @RestDescription("Gets a dummy")
    public Dummy getDummy(@PathVariable String pathVar, @RestDescription("The request parameter description") @RequestParam String requestVar, @RequestHeader("headerVarOverride") String headerVar) {
        return null;
    }

    @ResponseBody
    @RequestMapping("/dummy")
    @RestDescription("Gets a list of dummies")
    public List<Dummy> getDummies() {
        return null;
    }

    @RequestMapping(value = "/dummy", method = {RequestMethod.POST, RequestMethod.PUT})
    public void saveDummy(@RequestBody Dummy dummy, @SuppressWarnings("") String ignoredParameter) {

    }

    @RequestMapping(value = "/dummy/{pathVar}", method = {RequestMethod.DELETE})
    public void delete(@PathVariable String pathVarParamName) {

    }

    @RequestMapping(value = "/dummy/map", method = {RequestMethod.GET})
    public <T extends Dummy> Map<T, ? super Dummy> map() {
        return null;
    }

    @RequestMapping(value = "/dummy/map2", method = {RequestMethod.GET})
    public Map<X, Y> map2() {
        return null;
    }

    @RequestMapping(value = "/dummy/map3", method = {RequestMethod.GET})
    public <T extends X, V extends Y> Map<T, V> map3() {
        return null;
    }

    @RequestMapping(value = "/dummy/map4", method = {RequestMethod.GET})
    public Map<? extends X, ? super Y> map4() {
        return null;
    }

    public void noneRequestMappingMethod() {

    }

    @RequestMapping
    public void mappingMethodEmptyMapping() {

    }

}
