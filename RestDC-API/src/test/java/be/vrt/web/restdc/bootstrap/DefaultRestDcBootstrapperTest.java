package be.vrt.web.restdc.bootstrap;

import be.vrt.web.restdc.DocumentSetGenerator;
import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.store.DocumentSetStore;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class DefaultRestDcBootstrapperTest {
    private DefaultRestDcBootstrapper bootstrapper;

    @Mock
    private DocumentSetStore documentSetStore;

    @Mock
    private DocumentSetGenerator documentSetGeneratorX;

    @Mock
    private DocumentSetGenerator documentSetGeneratorY;

    @Before
    public void setUp() throws Exception {
        List<DocumentSetGenerator> generators = new ArrayList<>();
        generators.add(documentSetGeneratorX);
        generators.add(documentSetGeneratorY);
        bootstrapper = new DefaultRestDcBootstrapper(documentSetStore, generators);
    }

    @Test
    public void testBootstrap() throws Exception {
        DocumentSet documentSetX = new DocumentSet.DocumentSetBuilder("x").build();
        DocumentSet documentSetY = new DocumentSet.DocumentSetBuilder("y").build();

        when(documentSetGeneratorX.generate()).thenReturn(documentSetX);
        when(documentSetGeneratorY.generate()).thenReturn(documentSetY);

        bootstrapper.bootstrap();

        verify(documentSetStore).storeDocumentSet(documentSetX);
        verify(documentSetStore).storeDocumentSet(documentSetY);
    }
}
