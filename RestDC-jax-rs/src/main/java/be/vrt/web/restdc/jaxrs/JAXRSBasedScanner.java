package be.vrt.web.restdc.jaxrs;

import be.vrt.web.restdc.generator.AbstractPackageBasedDocumentSetGenerator;
import be.vrt.web.restdc.annotation.processor.AnnotationProcessor;
import be.vrt.web.restdc.domain.ResourceDocument;

import javax.ws.rs.Path;
import java.util.List;

/**
 * @author Mike Seghers
 */
public class JAXRSBasedScanner extends AbstractPackageBasedDocumentSetGenerator<Path> {
    public static final String JAX_RS_RESOURCES = "JAX RS Resources";

    /**
     * Constructor, initializing the document set generator for a given base package with an annotation processor.
     * @param basePackageName the package name
     * @param pathAnnotationProcessor the annotation processor
     */
    public JAXRSBasedScanner(final String basePackageName, final AnnotationProcessor<Path, List<ResourceDocument>, Class<?>> pathAnnotationProcessor) {
        super(basePackageName, Path.class, pathAnnotationProcessor);
    }

    @Override
    protected String getIdPrefix() {
        return JAX_RS_RESOURCES;
    }
}
