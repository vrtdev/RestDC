package be.vrt.web.restdc.spring;

import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.store.DocumentSetStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class RestDcControllerTest {
    private RestDcController controller;

    @Mock
    private DocumentSetStore documentSetStore;

    @Mock
    private Collection<DocumentSet> documentSets;

    @Before
    public void setUp() throws Exception {
        controller = new RestDcController(documentSetStore);
    }

    @Test
    public void testGetAllDocumentSets() throws Exception {
        when(documentSetStore.findAllDocumentSets()).thenReturn(documentSets);

        Collection<DocumentSet> allDocumentSets = controller.getAllDocumentSets();
        assertThat(allDocumentSets, is(sameInstance(documentSets)));
    }
}
