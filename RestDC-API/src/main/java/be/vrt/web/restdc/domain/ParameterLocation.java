package be.vrt.web.restdc.domain;

/**
 * @author Mike Seghers
 */
public enum ParameterLocation {
    /**
     * Parameter is part of the URL path.
     */
    PATH,
    /**
     * Parameter should be passed through as header value.
     */
    HEADER,
    /**
     * Parameter should be passed as request parameter.
     */
    PARAMETERS,
    /**
     * Parameter should be passed as body of the request.
     */
    BODY
}
