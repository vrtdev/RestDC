package be.vrt.web.restdc.jaxrs;

import be.vrt.web.restdc.annotation.processor.AnnotationProcessor;
import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.test.resources.NoneResource;
import be.vrt.web.restdc.test.resources.TestResource;
import be.vrt.web.restdc.test.resources.TestResourceOnlyMethodsHaveMapping;
import be.vrt.web.restdc.test.resources.TestResourceWithMime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class JAXRSBasedScannerTest {
    public static final String TEST_PACKAGE_TO_SCAN = "be.vrt.web.restdc.test.resources";
    private JAXRSBasedScanner scanner;

    @Mock
    private AnnotationProcessor<Path, List<ResourceDocument>, Class<?>> controllerAnnotationProcessor;

    @Before
    public void setUp() {
        scanner = new JAXRSBasedScanner(TEST_PACKAGE_TO_SCAN, controllerAnnotationProcessor);
    }

    @Test
    public void testGenerateWithPackage() throws Exception {
        List<ResourceDocument> testControllerDocuments = new ArrayList<>();
        testControllerDocuments.add(new ResourceDocument.ResourceDocumentBuilder().build());
        testControllerDocuments.add(new ResourceDocument.ResourceDocumentBuilder().build());
        List<ResourceDocument> testControllerWithMimeDocuments = new ArrayList<>();
        testControllerWithMimeDocuments.add(new ResourceDocument.ResourceDocumentBuilder().build());
        when(controllerAnnotationProcessor.process(Mockito.any(Path.class), Matchers.eq(TestResource.class)))
                .thenReturn(testControllerDocuments);
        when(controllerAnnotationProcessor.process(Mockito.any(Path.class), Matchers.eq(TestResourceWithMime.class)))
                .thenReturn(testControllerWithMimeDocuments);
        //Null should be returned for the TestControllerNoMapping class

        DocumentSet documentSet = scanner.generate();

        verify(controllerAnnotationProcessor, never())
                .process(Mockito.any(Path.class), Matchers.eq(NoneResource.class));
        verify(controllerAnnotationProcessor).process(Mockito.any(Path.class), Matchers.eq(TestResource.class));
        verify(controllerAnnotationProcessor).process(Mockito.any(Path.class), Matchers.eq(TestResourceWithMime.class));
        verify(controllerAnnotationProcessor, never())
                .process(Mockito.any(Path.class), Matchers.eq(TestResourceOnlyMethodsHaveMapping.class));

        assertThat(documentSet.getId(), is("JAX RS Resources in package " + TEST_PACKAGE_TO_SCAN));
        assertThat(documentSet.getDocuments(), hasSize(3));
    }
}
