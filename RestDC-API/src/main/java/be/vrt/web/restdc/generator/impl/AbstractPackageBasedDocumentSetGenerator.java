package be.vrt.web.restdc.generator.impl;

import be.vrt.web.restdc.annotation.processor.AnnotationProcessor;
import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.generator.DocumentSetGenerator;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * Base generator class, which generates document sets based on classes in a base package. This abstract class enforces the passage of a package and an
 * annotation type. Classes annotated with the specified annotation type will be further processed for document generation
 * (see {@link be.vrt.web.restdc.annotation.processor.AnnotationProcessor}). The document set will be based on information found in those classes.
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
     * Initialize the base package to be scanned and the annotation type to detect on the classes to
     * filter.
     *
     * @param basePackageName the package that should be scanned
     * @param annotationType  the annotation type
     */
    public AbstractPackageBasedDocumentSetGenerator(final String basePackageName, final Class<T> annotationType, final AnnotationProcessor<T, List<ResourceDocument>, Class<?>> annotationProcessor) {
        this.basePackageName = basePackageName;
        this.annotationType = annotationType;
        this.annotationProcessor = annotationProcessor;
    }

    @Override
    public final DocumentSet generate() {
        Reflections reflections = new Reflections(basePackageName);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(annotationType);
        return buildDocumentSetForClasses(classes);
    }

    private DocumentSet buildDocumentSetForClasses(final Set<Class<?>> classes) {
        LOGGER.trace("Generating DocumentSet with classes {} for base package {}", classes, basePackageName);
        DocumentSet.DocumentSetBuilder builder = new DocumentSet.DocumentSetBuilder(getIdPrefix() + " in package " + basePackageName);
        for (Class<?> annotatedClass : classes) {
            addResourceDocumentsForClass(builder, annotatedClass);
        }
        DocumentSet documentSet = builder.build();
        LOGGER.debug("Done generating DocumentSet: {}", documentSet);
        return documentSet;
    }

    private void addResourceDocumentsForClass(final DocumentSet.DocumentSetBuilder builder, final Class<?> annotatedClass) {
        T annotation = annotatedClass.getAnnotation(annotationType);
        List<ResourceDocument> documentsForClass = annotationProcessor.process(annotation, annotatedClass);
        if (documentsForClass != null) {
            builder.addAll(documentsForClass);
        } else {
            LOGGER.debug("No document resources found for class {}", annotatedClass);
        }
    }

    /**
     * Get an ID prefix for the generated document set identifier.
     *
     * @return an ID prefix
     */
    protected abstract String getIdPrefix();
}
