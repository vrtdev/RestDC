package be.vrt.web.restdc.jaxrs.annotation.processor.impl;

import be.vrt.web.restdc.annotation.processor.AnnotationProcessor;
import be.vrt.web.restdc.annotation.processor.OverridingAnnotationProcessor;
import be.vrt.web.restdc.domain.ResourceDocument;
import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Path;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Mike Seghers
 */
public class PathAnnotationProcessor implements AnnotationProcessor<Path, List<ResourceDocument>, Class<?>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PathAnnotationProcessor.class);

    private OverridingAnnotationProcessor<Path, ResourceDocument, Method, Class<?>> pathAnnotationProcessor;

    public PathAnnotationProcessor(final OverridingAnnotationProcessor<Path, ResourceDocument, Method, Class<?>> pathAnnotationProcessor) {
        this.pathAnnotationProcessor = pathAnnotationProcessor;
    }

    @Override
    public List<ResourceDocument> process(final Path pathAnnotation, final Class<?> annotatedElement) {
        LOGGER.debug("Generating resource documents for class {}", annotatedElement);
        Path classLevelRequestMapping = annotatedElement.getAnnotation(Path.class);
        Set<Method> allPathMethods = ReflectionUtils.getAllMethods(annotatedElement, ReflectionUtils.withAnnotation(Path.class));
        List<ResourceDocument> result;
        if (allPathMethods.size() == 0) {
            result = null;
        } else {
            result = new ArrayList<>(allPathMethods.size());
            for (Method requestMappingMethod : allPathMethods) {
                Path methodLevelPath = requestMappingMethod.getAnnotation(Path.class);
                result.add(pathAnnotationProcessor
                        .process(methodLevelPath, requestMappingMethod, classLevelRequestMapping, annotatedElement));
            }
        }

        LOGGER.debug("Done generating DocumentSets for class {}: {}", annotatedElement, result);
        return result;
    }
}
