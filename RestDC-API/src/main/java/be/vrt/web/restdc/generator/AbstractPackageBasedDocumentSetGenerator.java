package be.vrt.web.restdc.generator;

import be.vrt.web.restdc.annotation.processor.AnnotationProcessor;
import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.domain.ResourceDocument;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * Abstract package based document set generator. This abstract class enforces the passage of a package and an
 * annotation type. The annotation type will be used to filter the out those classes annotated with that annotation
 * type. The document set will be based on information found in those classes.
 *
 * @param <T> The annotation type
 * @author Mike Seghers
 */
public abstract class AbstractPackageBasedDocumentSetGenerator<T extends Annotation> implements DocumentSetGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPackageBasedDocumentSetGenerator.class);

    private String basePackageName;
    private Class<T> annotationType;

    private AnnotationProcessor<T, List<ResourceDocument>, Class<?>> annotationProcessor;

    /**
     * Constructor, initializing the base package to be scanned and the annotation type to detect on the classes to
     * filter.
     *
     * @param basePackageName the package that should be scanned
     * @param annotationType  the annotation type
     */
    public AbstractPackageBasedDocumentSetGenerator(final String basePackageName, final Class<T> annotationType, AnnotationProcessor<T, List<ResourceDocument>, Class<?>> annotationProcessor) {
        this.basePackageName = basePackageName;
        this.annotationType = annotationType;
        this.annotationProcessor = annotationProcessor;
    }

    @Override
    public final DocumentSet generate() {
        Reflections reflections = new Reflections(basePackageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotationType);

        LOGGER.trace("Generating DocumentSet with classes {} for base package {}", classes, basePackageName);
        DocumentSet.DocumentSetBuilder builder = new DocumentSet.DocumentSetBuilder(getIdPrefix() + " in package " + basePackageName);
        List<ResourceDocument> documentsForClass;
        for (Class<?> annotatedClass : classes) {
            T annotation = annotatedClass.getAnnotation(annotationType);
            documentsForClass = annotationProcessor.process(annotation, annotatedClass);
            if (documentsForClass != null) {
                builder.addAll(documentsForClass);
            } else {
                LOGGER.debug("No document resources found for class {}", annotatedClass);
            }
        }

        DocumentSet documentSet = builder.build();
        LOGGER.debug("Done generating DocumentSet: {}", documentSet);
        return documentSet;
    }

    /**
     * Get an ID prefix for the generated document set identifier.
     *
     * @return an ID prefix
     */
    protected abstract String getIdPrefix();
}
