package be.vrt.web.restdc.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A DocumentSet holds all {@link be.vrt.web.restdc.domain.ResourceDocument}s on a REST API that belong together.
 *
 * To create instances of this class, you'll need to use it's builder: {@link be.vrt.web.restdc.domain.DocumentSet.DocumentSetBuilder}
 *
 * @author Mike Seghers
 */
public class DocumentSet {
    private String id;
    private List<ResourceDocument> documents;

    private DocumentSet(final String id, final List<ResourceDocument> documents) {
        this.id = id;
        this.documents = Collections.unmodifiableList(documents);
    }

    public String getId() {
        return id;
    }

    public List<ResourceDocument> getDocuments() {
        return documents;
    }

    @Override
    public String toString() {
        return "DocumentSet{" +
                "id='" + id + '\'' +
                ", documents=" + documents +
                '}';
    }

    /**
     * {@link DocumentSet} builder
     */
    public static class DocumentSetBuilder {
        private String id;
        private List<ResourceDocument> documents = new ArrayList<>();

        /**
         * Constructor, initializing the identifier for the document set to be build.
         * @param id the identifier
         */
        public DocumentSetBuilder(final String id) {
            this.id = id;
        }

        /**
         * Add a ResourceDocument to the builder
         *
         * @param document the document to be added
         * @return the builder itself
         */
        public DocumentSetBuilder addDocument(final ResourceDocument document) {
            this.documents.add(document);
            return this;
        }

        /**
         * Add all given ResourceDocuments to the builder
         *
         * @param documents all ResourceDocuments to be added
         */
        public DocumentSetBuilder addAll(final List<ResourceDocument> documents) {
            this.documents.addAll(documents);
            return this;
        }

        /**
         * Builds the DocumentSet
         *
         * @return the DocumentSet
         */
        public DocumentSet build() {
            return new DocumentSet(id, documents);
        }
    }
}
