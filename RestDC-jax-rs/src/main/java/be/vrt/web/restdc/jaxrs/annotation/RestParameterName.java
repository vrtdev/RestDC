package be.vrt.web.restdc.jaxrs.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * REST DC annotation, specific for JAX RS to annotate request body method parameters so that they also can have a name.
 *
 * @author Mike Seghers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Documented
public @interface RestParameterName {
    /**
     * The name of the parameter in the rest api method
     */
    String value();
}
