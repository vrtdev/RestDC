package be.vrt.web.restdc.bootstrap;

import be.vrt.web.restdc.DocumentSetGenerator;
import be.vrt.web.restdc.store.DocumentSetStore;

import java.util.List;

/**
 * Default rest dc bootstrapper implementation. This implementation loads {@link be.vrt.web.restdc.domain.DocumentSet}s
 * from the configured list of {@link be.vrt.web.restdc.DocumentSetGenerator}s and stores them in the configured {@link
 * be.vrt.web.restdc.store.DocumentSetStore}.
 *
 * @author Mike Seghers
 */
public class DefaultRestDcBootstrapper implements RestDcBootstrapper {
    private DocumentSetStore documentSetStore;

    private List<DocumentSetGenerator> documentSetGenerators;

    /**
     * Constructor, initializing this bootstrapper with a DocumentSetStore and a list of DocumentSetGenerators.
     * @param documentSetStore the DocumentSet store
     * @param documentSetGenerators the DocumentSet generator
     */
    public DefaultRestDcBootstrapper(final DocumentSetStore documentSetStore, final List<DocumentSetGenerator> documentSetGenerators) {
        this.documentSetStore = documentSetStore;
        this.documentSetGenerators = documentSetGenerators;
    }

    @Override
    public void bootstrap() {
        for (DocumentSetGenerator generator : documentSetGenerators) {
            documentSetStore.storeDocumentSet(generator.generate());
        }
    }
}
