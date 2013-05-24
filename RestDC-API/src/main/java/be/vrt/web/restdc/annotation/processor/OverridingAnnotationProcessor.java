package be.vrt.web.restdc.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Interface to enable annotation processing for an {@link java.lang.annotation.Annotation} of type T on an {@link
 * java.lang.reflect.AnnotatedElement} of type AE, resulting in an object of type R. The processing can further look
 * into a base {@link java.lang.annotation.Annotation} of type T on an {@link java.lang.reflect.AnnotatedElement} of
 * type BAE.
 *
 * @param <T>   the annotation type
 * @param <R>   the result type
 * @param <AE>  the AnnotatedElement type on which the annotation was found
 * @param <BAE> the AnnotatedElement type on which the base annotation was found
 * @author Mike Seghers
 */
public interface OverridingAnnotationProcessor<T extends Annotation, R, AE extends AnnotatedElement, BAE extends AnnotatedElement> {
    /**
     * Process the annotations, which were found on the given origin and baseOrigin. Callers of this method should
     * re-assure that the
     * origin is indeed the origin of the annotation! The base origin will typically serve as default value provider,
     * while the regular origin will serve the effective values and/or overrides and/or additions.
     *
     * @param annotation the annotation to process
     * @param origin     the origin of the given annotation
     * @param baseAnnotation the base annotation to process
     * @param baseOrigin the origin of the given base annotation
     * @return the processing result
     */
    R process(T annotation, AE origin, T baseAnnotation, BAE baseOrigin);
}
