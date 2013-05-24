package be.vrt.web.restdc.store;

import be.vrt.web.restdc.domain.DocumentSet;

import java.util.Collection;

/**
 * @author Mike Seghers
 */
public interface DocumentSetStore {
    /**
     * Looks up a document set for the specified id.
     *
     * @param id the identifier of the wanted documented set
     * @return the document set with the given identifier, or null if no document set with the given identifier could
     *         be
     *         found.
     */
    DocumentSet findDocumentSet(String id);

    /**
     * Returns all document sets persisted in this store.
     *
     * @return all document sets persisted in this store
     */
    Collection<DocumentSet> findAllDocumentSets();

    /**
     * Persist a document set in this store. If the store already has a document set with the same identifier, the given
     * document set will replace the existing one.
     *
     * @param documentSet the document set to persist.
     */
    void storeDocumentSet(DocumentSet documentSet);
}
