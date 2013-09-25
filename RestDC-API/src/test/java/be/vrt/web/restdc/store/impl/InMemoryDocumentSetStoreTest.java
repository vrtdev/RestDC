package be.vrt.web.restdc.store.impl;

import be.vrt.web.restdc.domain.DocumentSet;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * @author Mike Seghers
 */
public class InMemoryDocumentSetStoreTest {

    private InMemoryDocumentSetStore store;

    @Before
    public void setUp() throws Exception {
        store = new InMemoryDocumentSetStore();
    }

    @Test
    public void test_store() throws Exception {
        store.storeDocumentSet(new DocumentSet.DocumentSetBuilder("id").build());
        Collection<DocumentSet> all = store.findAllDocumentSets();
        assertThat(all, hasSize(1));
    }

    @Test
    public void test_findById() throws Exception {
        DocumentSet ds = new DocumentSet.DocumentSetBuilder("id").build();
        store.storeDocumentSet(ds);
        DocumentSet found = store.findDocumentSet("id");
        assertThat(found, is(sameInstance(ds)));
    }
}
