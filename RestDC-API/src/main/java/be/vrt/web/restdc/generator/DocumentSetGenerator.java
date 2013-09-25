package be.vrt.web.restdc.generator;

import be.vrt.web.restdc.domain.DocumentSet;

/**
 * Implementations should generate a document set.
 *
 * @author Mike Seghers
 */
public interface DocumentSetGenerator {
    /**
     * Generates a DocumentSet.
     * @return the generated document set
     */
    DocumentSet generate();
}
