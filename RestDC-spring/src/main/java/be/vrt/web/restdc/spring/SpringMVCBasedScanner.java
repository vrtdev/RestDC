package be.vrt.web.restdc.spring;

import be.vrt.web.restdc.AbstractPackageBasedDocumentSetGenerator;
import be.vrt.web.restdc.annotation.processor.AnnotationProcessor;
import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.domain.ResourceDocument;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

/**
 * {@link be.vrt.web.restdc.DocumentSetGenerator} implementation for Spring MVC based generation of a REST API {@link
 * be.vrt.web.restdc.domain.DocumentSet}.
 * <p/>
 * This generator picks up all classes in a given package annotated with {@link org.springframework.stereotype.Controller}.
 * In these classes, for each methods annotated with {@link org.springframework.web.bind.annotation.RequestMapping}, a
 * {@link be.vrt.web.restdc.domain.ResourceDocument} is generated.
 * <p/>
 * This class makes heavy use of reflection, reading Spring MVC and REST DC annotations and evaluating generic types.
 * It also makes user of Spring's {@link org.springframework.core.ParameterNameDiscoverer} to lookup parameter names.
 * When parameter names
 * cannot be discovered, the developer of the {@link org.springframework.stereotype.Controller} class should always add
 * the name of the parameter in the specific annotation values (such as
 * {@link org.springframework.web.bind.annotation.PathVariable},
 * {@link org.springframework.web.bind.annotation.RequestParam}, etc).
 * <p/>
 * Here's a list of annotations this class uses:
 * <p/>
 * <ul>
 * <li>{@link org.springframework.stereotype.Controller}</li>
 * <li>{@link org.springframework.web.bind.annotation.PathVariable}</li>
 * <li>{@link org.springframework.web.bind.annotation.RequestBody}</li>
 * <li>{@link org.springframework.web.bind.annotation.RequestHeader}</li>
 * <li>{@link org.springframework.web.bind.annotation.RequestMapping}</li>
 * <li>{@link org.springframework.web.bind.annotation.RequestParam}</li>
 * <li>{@link be.vrt.web.restdc.annotation.RestDescription}</li>
 * </ul>
 *
 * @author Mike Seghers
 */
public class SpringMVCBasedScanner extends AbstractPackageBasedDocumentSetGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpringMVCBasedScanner.class);

    private AnnotationProcessor<Controller, List<ResourceDocument>, Class<?>> controllerAnnotationProcessor;

    /**
     * Constroctor, initializing the package to scan and the {@link be.vrt.web.restdc.annotation.processor.AnnotationProcessor}
     * which processes {@link org.springframework.stereotype.Controller} annotations to get a list of {@link
     * be.vrt.web.restdc.domain.ResourceDocument} based on the information found in that specific class.
     *
     * @param basePackageName               the package name to scan
     * @param controllerAnnotationProcessor the controller annotation processor
     */
    public SpringMVCBasedScanner(final String basePackageName, final AnnotationProcessor<Controller, List<ResourceDocument>, Class<?>> controllerAnnotationProcessor) {
        super(basePackageName);
        this.controllerAnnotationProcessor = controllerAnnotationProcessor;
    }

    @Override
    protected DocumentSet generateWithPackage(final String basePackageName) {
        LOGGER.trace("Generating DocumentSet with package {}", basePackageName);
        DocumentSet.DocumentSetBuilder builder = new DocumentSet.DocumentSetBuilder(basePackageName);
        Set<Class<?>> classes = getAnnotatedClassesInPackage(Controller.class, basePackageName);
        List<ResourceDocument> documentsForClass;
        for (Class<?> controllerAnnotatedClass : classes) {
            Controller annotation = controllerAnnotatedClass.getAnnotation(Controller.class);
            documentsForClass = controllerAnnotationProcessor.process(annotation, controllerAnnotatedClass);
            if (documentsForClass != null) {
                builder.addAll(documentsForClass);
            } else {
                LOGGER.debug("No document resources found for class {}", controllerAnnotatedClass);
            }
        }

        DocumentSet documentSet = builder.build();
        LOGGER.debug("Done generating DocumentSet: {}", documentSet);
        return documentSet;
    }

    /**
     * Returns all classes in a given package which are annotated with the given annotation.
     *
     * @param annotationType the type of the annotation to look for
     * @param packageName    the name of the package to scan
     * @return all classes annotated with the given annotation
     */
    private Set<Class<?>> getAnnotatedClassesInPackage(final Class<? extends Annotation> annotationType, final String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(annotationType);
    }
}
