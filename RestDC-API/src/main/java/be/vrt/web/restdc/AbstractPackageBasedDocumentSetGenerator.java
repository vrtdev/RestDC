package be.vrt.web.restdc;

import be.vrt.web.restdc.domain.DocumentSet;

/**
 * @author Mike Seghers
 */
public abstract class AbstractPackageBasedDocumentSetGenerator implements DocumentSetGenerator {
    private String basePackageName;

    /**
     * Constructor, initializing the base package to be scanned.
     *
     * @param basePackageName the package that should be scanned
     */
    public AbstractPackageBasedDocumentSetGenerator(final String basePackageName) {
        this.basePackageName = basePackageName;
    }

    @Override
    public final DocumentSet generate() {
        return generateWithPackage(basePackageName);
    }

    protected abstract DocumentSet generateWithPackage(String basePackageName);
}
