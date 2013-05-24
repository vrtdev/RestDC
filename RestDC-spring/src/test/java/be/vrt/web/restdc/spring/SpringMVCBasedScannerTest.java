package be.vrt.web.restdc.spring;

import be.vrt.web.restdc.annotation.processor.AnnotationProcessor;
import be.vrt.web.restdc.domain.DocumentSet;
import be.vrt.web.restdc.domain.ResourceDocument;
import be.vrt.web.restdc.test.controllers.NoneController;
import be.vrt.web.restdc.test.controllers.TestController;
import be.vrt.web.restdc.test.controllers.TestControllerNoMapping;
import be.vrt.web.restdc.test.controllers.TestControllerWithMime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Mike Seghers
 */
@RunWith(MockitoJUnitRunner.class)
public class SpringMVCBasedScannerTest {
    public static final String TEST_PACKAGE_TO_SCAN = "be.vrt.web.restdc.test.controllers";
    private SpringMVCBasedScanner scanner;

    @Mock
    private AnnotationProcessor<Controller, List<ResourceDocument>, Class<?>> controllerAnnotationProcessor;

    @Before
    public void setUp() {
        scanner = new SpringMVCBasedScanner(TEST_PACKAGE_TO_SCAN, controllerAnnotationProcessor);
    }

    @Test
    public void testGenerateWithPackage() throws Exception {
        List<ResourceDocument> testControllerDocuments = new ArrayList<>();
        testControllerDocuments.add(new ResourceDocument.ResourceDocumentBuilder().build());
        testControllerDocuments.add(new ResourceDocument.ResourceDocumentBuilder().build());
        List<ResourceDocument> testControllerWithMimeDocuments = new ArrayList<>();
        testControllerWithMimeDocuments.add(new ResourceDocument.ResourceDocumentBuilder().build());
        when(controllerAnnotationProcessor.process(Mockito.any(Controller.class), Matchers.eq(TestController.class)))
                .thenReturn(testControllerDocuments);
        when(controllerAnnotationProcessor
                .process(Mockito.any(Controller.class), Matchers.eq(TestControllerWithMime.class)))
                .thenReturn(testControllerWithMimeDocuments);
        //Null should be returned for the TestControllerNoMapping class

        DocumentSet documentSet = scanner.generate();

        verify(controllerAnnotationProcessor, never())
                .process(Mockito.any(Controller.class), Matchers.eq(NoneController.class));
        verify(controllerAnnotationProcessor).process(Mockito.any(Controller.class), Matchers.eq(TestController.class));
        verify(controllerAnnotationProcessor)
                .process(Mockito.any(Controller.class), Matchers.eq(TestControllerWithMime.class));
        verify(controllerAnnotationProcessor)
                .process(Mockito.any(Controller.class), Matchers.eq(TestControllerNoMapping.class));

        assertThat(documentSet.getId(), is(TEST_PACKAGE_TO_SCAN));
        assertThat(documentSet.getDocuments(), hasSize(3));
    }
}
