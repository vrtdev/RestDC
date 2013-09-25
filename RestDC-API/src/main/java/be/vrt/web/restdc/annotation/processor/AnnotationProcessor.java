package be.vrt.web.restdc.annotation.processor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Interface to enable annotation processing for an {@link java.lang.annotation.Annotation} of type T on an {@link
 * java.lang.reflect.AnnotatedElement} of type AE, resulting in an object of type R.
 *
 * @param <T>  the annotation type
 * @param <R>  the result type
 * @param <AE> the AnnotatedElement type on which the annotation was found
 * @author Mike Seghers
 */
public interface AnnotationProcessor<T extends Annotation, R, AE extends AnnotatedElement> {
    /**
     * Process the annotation, which was found on the given annotatedElement. Callers of this method should re-assure that the
     * annotatedElement is indeed the element annotated with the annotation.
     *
     * @param annotation       the annotation to process
     * @param annotatedElement the annotated element of the given annotation
     * @return the processing result
     */
    R process(T annotation, AE annotatedElement);


}
