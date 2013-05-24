package be.vrt.web.restdc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to generate REST API documentation at runtime for a REST enabling class.
 *
 * @author Mike Seghers
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.PARAMETER})
@Documented
public @interface RestDescription {
    /**
     * The description for a REST API call.
     */
    String value();
}
