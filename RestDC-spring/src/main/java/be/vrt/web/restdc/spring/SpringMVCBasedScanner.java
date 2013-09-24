package be.vrt.web.restdc.spring;

import be.vrt.web.restdc.generator.AbstractPackageBasedDocumentSetGenerator;
import be.vrt.web.restdc.annotation.processor.AnnotationProcessor;
import be.vrt.web.restdc.domain.ResourceDocument;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * {@link be.vrt.web.restdc.generator.DocumentSetGenerator} implementation for Spring MVC based generation of a REST API {@link
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
public class SpringMVCBasedScanner extends AbstractPackageBasedDocumentSetGenerator<Controller> {

    public static final String SPRING_CONTROLLERS = "Spring Controllers";

    /**
     * Constructor, initializing the package to scan and the {@link be.vrt.web.restdc.annotation.processor.AnnotationProcessor}
     * which processes {@link org.springframework.stereotype.Controller} annotations to get a list of {@link
     * be.vrt.web.restdc.domain.ResourceDocument} based on the information found in that specific class.
     *
     * @param basePackageName               the package name to scan
     * @param controllerAnnotationProcessor the controller annotation processor
     */
    public SpringMVCBasedScanner(final String basePackageName, final AnnotationProcessor<Controller, List<ResourceDocument>, Class<?>> controllerAnnotationProcessor) {
        super(basePackageName, Controller.class, controllerAnnotationProcessor);
    }

    @Override
    protected String getIdPrefix() {
        return SPRING_CONTROLLERS;
    }
}
