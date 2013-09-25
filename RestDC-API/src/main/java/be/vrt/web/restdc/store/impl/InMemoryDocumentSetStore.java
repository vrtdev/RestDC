package be.vrt.web.restdc.store.impl;

import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.store.DocumentSetStore;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * DocumentSetStore implementation, which simply keeps documents in memory. Note that this class holds a registry at the
 * instance level. You should therefore always re-use the same instance of this class if you want to build up an entire
 * store.
 *
 * @author Mike Seghers
 */
public class InMemoryDocumentSetStore implements DocumentSetStore {
    private Map<String, DocumentSet> registry;

    /**
     * Initializes the in memory store.
     */
    public InMemoryDocumentSetStore() {
        registry = new HashMap<>();
    }

    @Override
    public DocumentSet findDocumentSet(final String id) {
        return registry.get(id);
    }

    @Override
    public Collection<DocumentSet> findAllDocumentSets() {
        return registry.values();
    }

    @Override
    public void storeDocumentSet(final DocumentSet documentSet) {
        registry.put(documentSet.getId(), documentSet);
    }
}
